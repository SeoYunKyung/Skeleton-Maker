from dogDetector import DogDetector
import numpy as np
import matplotlib.pyplot as plt
import imageProcessor
import cv2
import os
import sys
import dlib
import io

#faces_folder = sys.argv[1]
#split_folder = faces_folder.split('/')
#fake_img_name = split_folder[len(split_folder)-1].split('.') ##name with *jpg, *jpeg *bmp
#real_img_name = fake_img_name[0] ## name without *jpg *jpeg *bmp

imgPath = sys.argv[1]
imgrealPath = "/home/seoyunkyung/dang/wikiCloggy_server/"+imgPath

dd = DogDetector()

img = cv2.imread(imgrealPath)
img_result = dd.detectsOneDog(img)

if img_result ==False : ## can't detect dog
	print("can't detect dog")
	quit()

#win = dlib.image_window()
#win.clear_overlay()
#win.set_image(img)
#win.add_overlay(dets)
#dlib.hit_enter_to_continue()

rect = dd.getDogRect(result, img) # (x,y,width,height)
#print("Rect is %d, %d, %d, %d" %rect)

doghead = dd.detectDoghead(img) 
doghead_rect = dd.getDogPartRect(dets_pop, img)

dogAreaImage = imageProcessor.drawRectangle(img, rect)
#dogAreaImage = imageProcessor.drawRectangle(dogAreaImage)
if(!dd.isLeft):
	dogAreaImage = cv2.flip(dogAreaImage,1)

dogAreaImage = cv2.cvtColor(dogAreaImage, cv2.COLOR_BGR2RGB)

plt.title('Dog Area')
#plt.imshow(dogAreaImage)
#plt.show()

mask = imageProcessor.deleteBackground(img, rect)

#plt.title('result')
#plt.imshow(mask, 'gray')
#plt.show()

cv2.imwrite('../wikiCloggy_skeletonMaker/result/'+real_img_name+'.png', mask * 255)
cv2.imwrite('../wikiCloggy_skeletonMaker/result/result.png',mask*255)
result_list = list(dets)

os.system('cd ../wikiCloggy_skeletonMaker; ./run.sh')

