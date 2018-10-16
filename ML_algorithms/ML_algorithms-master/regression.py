import pandas as pd
import math
import quandl
import datetime , time
import numpy as np
from sklearn import preprocessing , cross_validation , svm
from sklearn.linear_model import LinearRegression
from sklearn.model_selection import train_test_split
import matplotlib.pyplot as plt
from matplotlib import style
import pickle

style.use('ggplot')

df = quandl.get('WIKI/GOOGL')
df['HL_PCT'] = (df['Adj. High'] - df['Adj. Close']) / df['Adj. Close'] * 100

df['change_PCT'] = (df['Adj. Close'] - df['Adj. Open']) / df['Adj. Open'] * 100

df = df[['Adj. Close' , 'HL_PCT' , 'change_PCT']]

forecast_col = 'Adj. Close'
df.fillna(-99999 , inplace=True)

forecast_out = int(math.ceil(0.01*len(df)))

df['label'] = df[forecast_col].shift(-forecast_out)


X = np.array(df.drop(['label'] , 1))

X = preprocessing.scale(X)

X = X[:-forecast_out]
X_lately = X[-forecast_out:]

df.dropna(inplace=True)

y = np.array(df['label'])
#print len(X)
#print len(y)
X_train , X_test , y_train , y_test = cross_validation.train_test_split(X , y , test_size=0.2)
#print (len(X_train) , len(X_test) , len(y_train) , len(y_test))
classifier = LinearRegression(n_jobs=-1)
classifier.fit(X_train , y_train)

#pickling the classifier
with open('linearregression.pickle','wb') as f:
	pickle.dump(classifier, f)

pickle_in = open('linearregression.pickle','rb')
classifier = pickle.load(pickle_in)

accuracy = classifier.score(X_test , y_test)
#print accuracy
forecast_set = classifier.predict(X_lately)
#print (forecast_set , accuracy , forecast_out)
df['forecast'] = np.nan

last_date = df.iloc[-1].name
last_unix = time.mktime(last_date.timetuple())
one_day = 86400
next_unix = last_unix + one_day
for i in forecast_set:
	next_date = datetime.datetime.fromtimestamp(next_unix)
	next_unix = next_unix + one_day
	df.loc[next_date] = [np.nan for _ in range(len(df.columns)-1)] + [i]
	
df['Adj. Close'].plot()
df['forecast'].plot()
plt.legend(loc=4)
plt.xlabel('Date')
plt.ylabel('Price')
plt.show()
