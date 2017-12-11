# smartnation-tensorflow
Backend of Smart Nation Information Systems 1D Project

## Instructions
Run App.java, which will pull images every 30 seconds from a running Raspberry Pi camera set up in the canteen and evaluate it with our pre-trained TensorFlow model.

User must configure the directories of imgDir and modelDir with App.java to run the model on their own computers. Redis/Android Application code is separate and can be found on Yao Jie's repo - https://github.com/causztic/smartnation-app and https://github.com/causztic/smartnation.

## Additional Information
Access to the live camera stream as used by ImageReader.java is limited to SUTD's local network, as it is hosted within the school. Fnctionaltiy is also subject to the presence of the Raspberry Pi - since I'll likely be removing it for other projects in the future.

## Large Files
Due to git protocols, files larger than 100MB cannot be uploaded. As a result, they are hosted at the following link: https://drive.google.com/open?id=1PvqMwNQMgQnXoVCpk7xH1A1MXKcyrSZb
Test and Training files (in the Model Building folder) are zipped, because otherwise cloning the repo would take ages - about 45000 test/train 10 KB images in total. Unzipping will still take a while on the user end, but still requires less time. This is ONLY NECESSARY if you want to use the test/train set to train new models; our existing model is alraedy part of the repo, under the "model" folder.

The model itself can also be downloaded from the same Google Drive link. The model's predictions have a standard deviation of 0.8 from a 0 to 9 scale on canteen crowdedness.
