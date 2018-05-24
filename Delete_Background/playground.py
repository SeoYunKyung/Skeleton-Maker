from dogDetector import DogDetector
import numpy as np
import matplotlib.pyplot as plt
import imageProcessor
import cv2
import os
import sys
import dlib
import io

faces_folder = sys.argv[1]

dd = DogDetector()

img = cv2.imread(faces_folder)

result = dd.detectsOneDog(img)
print(result)

#win = dlib.image_window()

options = dlib.simple_object_detector_training_options()
options.add_left_right_image_flips = True
options.C = 7
options.num_threads = 4
options.be_verbose = True
options.epsilon = 0.01

detector = dlib.simple_object_detector("dog_detector.svm")
dets = detector(img)


#win.clear_overlay()
#win.set_image(img)
#win.add_overlay(dets)
#dlib.hit_enter_to_continue()

#dog1__.jpg
#rect = (28 + 25, 2 + 25, 517 - 50, 447 - 50)
#rect = (28, 2, 517, 422)
#dog3.jpg
#rect = (113, 34, 325, 305)
#sample_dog.jpg
#rect = (111, 189, 236, 375)

rect = dd.getDogRect(result, img)
#print("Rect is %d, %d, %d, %d" %rect)
if(len(dets) == 0):
	print("Can't detect Head")
	quit()
else :
	dets_pop = dets.pop()
dets_rect = dd.getDogPartRect(dets_pop, img)

dogAreaImage = imageProcessor.drawRectangle(img, rect)
dogAreaImage = imageProcessor.drawRectangle(dogAreaImage,dets_rect)
dogAreaImage = cv2.cvtColor(dogAreaImage, cv2.COLOR_BGR2RGB)

plt.title('Dog Area')
plt.imshow(dogAreaImage)
plt.show()

mask = imageProcessor.deleteBackground(img, rect)

#plt.title('result')
#plt.imshow(mask, 'gray')
#plt.show()

cv2.imwrite('../../wikiCloggy_skeletonMaker/result/result.png', mask * 255)
result_list = list(dets)
fw = open('../../wikiCloggy_skeletonMaker/result/result.txt','w')

fw.write('t '+str(dets_pop.top())+' l '+str(dets_pop.left())+' b '+str(dets_pop.bottom())+' r '+str(dets_pop.right()))
fw.close()
os.system('cd ../../wikiCloggy_skeletonMaker; ./run.sh')

