from __future__ import print_function
from imutils.object_detection import non_max_suppression
from imutils import paths
import numpy as np
import argparse
import imutils
import cv2

cam = cv2.VideoCapture(0)
cam.open(0)
hog = cv2.HOGDescriptor()
hog.setSVMDetector(cv2.HOGDescriptor_getDefaultPeopleDetector())
cy1=0
while True:
    
  #  image = cv2.imread("C:/Users/dell/Desktop/a.jpg")
    ret ,image = cam.read()
    image = imutils.resize(image, width=min(400, image.shape[1]))
    orig = image.copy()
 
        # detect people in the image
    (rects, weights) = hog.detectMultiScale(image, winStride=(4,4),padding=( 10 ,10), scale=1.05)
 
        # draw the original bounding boxes
    for (x, y, w, h) in rects:
        cv2.rectangle(orig, (x, y), (x + w, y + h), (0, 0, 255), 2)
       # print ((x+x+w)/2)
 
    rects = np.array([[x, y, x + w, y + h] for (x, y, w, h) in rects])
    pick = non_max_suppression(rects, probs=None, overlapThresh=12.95)
 
        # draw the final bounding boxes
    for (xA, yA, xB, yB) in pick:
        cv2.rectangle(image, (xA, yA), (xB, yB), (0, 255, 0), 2)
        cy1 = float((xB + xA)/2)
        cxx = (yB + yA)/2
        
        #r,c,ch = image.shape
        print (cy1)
        #print (r,c,ch)
            # show the output images
       # print ((xA+xB)/2)    

    cv2.imshow("output", image)
    if cv2.waitKey(1) & 0xFF == ord ('q'):
        break
b=float(300)             #view angle distance
b1= float(300)
m1=float(200)
m=float(200)

if m>cy1:
    a= float(m-cy1)
else:
    a=float(cy1-m)
print (a)
a1=float(50)
x=(((1+(b*b1)/(a*a1))*m)-(m1*((b/a) -1)*(b1/a1)))/(1+(b*b1)/(a*a1))
x1=(m1*((b/a)-1))/(1+(b*b1)/(a*a1))
if m>x:
   Y=m-x
else:
   Y=x+m
if m1>x1:
   X=m1-x1
else:
   X=x1+m1
print (X, Y)


   # cv2.waitKey(10)
    
#cam = cv2.VideoCapture()
#cam.open(0)
#ret ,image = cam.read()
#cv2.imshow( 'fghjkl'  , image)
cam.release()
cv2.destroyAllWindows()
