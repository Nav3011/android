import numpy as np
import pandas as pd
from sklearn import preprocessing , cross_validation , neighbors

df = pd.read_csv('dataset.txt')
df.replace('?' , -99999 , inplace = True)
df.drop(['id'] , 1, inplace=True)

X = np.array(df.drop(['class'] , 1))
y = np.array(df['class'])

X_train , X_test , y_train , y_test = cross_validation.train_test_split(X , y , test_size = 0.2)

classifier = neighbors.KNeighborsClassifier()
classifier.fit(X_train , y_train)

accuracy = classifier.score(X_test , y_test)

print (accuracy) 

example_measures = np.array([[4,2,3,6,1,1,3,1,4] , [2,2,3,6,3,1,3,1,1]])
prediction = classifier.predict(example_measures)

print (prediction)
