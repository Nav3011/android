import numpy as np
import matplotlib.pyplot as plt
from matplotlib import style
from statistics import mean

I = np.random.randint(12, size=15)
O = np.random.randint(0,10, size=15)

class Linear_regression(object):
    def __init__(self, inputs, labels):
        self.X = inputs
        self.Y = labels
        print self.X
        print self.Y

    def mean(self,A):
        return sum(A)/len(A)
    def best_fit_slope(self):
        m = (mean(self.X*self.Y) - mean(self.X)*mean(self.Y))/(mean(self.X**2) - mean(self.X)**2)
        return m

    def best_fit_intercept(self):
        c = mean(self.Y) - self.best_fit_slope()*mean(self.X)
        return c

    def regression_line(self):
        slope = self.best_fit_slope()
        intercept = self.best_fit_intercept()
        line = (slope * self.X) + intercept
        return line
    
plt.plot(I,O, 'ro')
LR = Linear_regression(I,O)
points = LR.regression_line()
plt.plot(I,points)
plt.show()