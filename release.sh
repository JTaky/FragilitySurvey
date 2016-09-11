
if [ "$#" -ne 1 ]; then
    echo "Please pass suffix, e.g."
    echo "./release.sh 7"
    exit
fi

"/Applications/Android Studio.app/Contents/gradle/gradle-2.14.1/bin/gradle" clean assemble

rm ../../../Google\ Drive/MyProjects/McGill/builds/*apk
cp app/build/outputs/apk/app-debug.apk ../../../Google\ Drive/MyProjects/McGill/builds/app-debug-$1.apk
ls -l ../../../Google\ Drive/MyProjects/McGill/builds/
echo "released, press any key to share"
read
/usr/bin/open -a "/Applications/Google Chrome.app" "https://drive.google.com/drive/folders/0B3i1E9ktLoI6QndwaXNvTEp5aXM"
