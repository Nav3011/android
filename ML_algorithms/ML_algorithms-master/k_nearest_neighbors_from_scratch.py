import numpy as np
import warnings
import matplotlib.pyplot as plt
from collections import Counter
from matplotlib import style
import pandas as pd
import random

style.use('fivethirtyeight')

dataset = {'k' : [[1,2], [2,3], [3,1]] , 'r' : [[6,5], [7,7], [8,6]] }  # sample dataset
new_features = [5,7]   #classify the new feature

for i in dataset:
	for j in dataset[i]:
		plt.scatter(j[0] , j[1] , s=100 , color=i)
		
def k_nearest_neighbors(data , predict , k=3):
        distances=[]
        for group in data:
                for features in data[group]:
                        euclidean_distance = np.linalg.norm(np.array(features) - np.array(predict))    #calculating the distances between the point to be predicted and the other points
                        distances.append([euclidean_distance , group])   #list of all the distances
                        
        votes = [i[1] for i in sorted(distances)[:k]]    #stores the distances in sorted order
        print Counter(votes).most_common(1) #find the class to which the prediction belongs to by deciding which has more number of votes
        vote_result = Counter(votes).most_common(1)[0][0]
        return vote_result

vote = k_nearest_neighbors(dataset , new_features , k=3)
print vote

plt.scatter(new_features[0], new_features[1], color=vote)
plt.show()






