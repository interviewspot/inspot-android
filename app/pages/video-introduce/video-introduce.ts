import {Component} from '@angular/core';
import {NavController, Storage, LocalStorage, Loading, Alert} from 'ionic-angular';
import {HttpService} from '../../services/http.service';
import {CONFIG} from '../../config/config';
import {Camera} from 'ionic-native';



@Component({
    templateUrl: 'build/pages/video-introduce/video-introduce.html',
})
export class VideoIntroducePage {
    local:Storage = new Storage(LocalStorage);
    user:any;
    imgURI:any;
    showVideo = false;

    constructor(public nav:NavController, private httpService:HttpService) {
        
    }

    startLoading() {
        //loading
        let loading = Loading.create({
            dismissOnPageChange: true
        });
        this.nav.present(loading);
        return loading;
    }

    endLoading(loading) {
        loading.dismiss();
    }

    alert(message) {
        //alert message
        let alert = Alert.create({
            title: 'Login fail!',
            message: message,
            buttons: ['Ok']
        });
        this.nav.present(alert);
    }

    startCamera(){

            this.showVideo = true;
        // var options = {
        //     quality : 75,
        //     destinationType : Camera.DestinationType.FILE_URI,
        //     sourceType : Camera.PictureSourceType.CAMERA,
        //     targetWidth: 300,
        //     targetHeight: 300,
        //     saveToPhotoAlbum: false,
        //     mediaType: Camera.MediaType.VIDEO
        // };
        // Camera.getPicture(options).then((imageData) => {
        //     // imageData is either a base64 encoded string or a file URI
        //     // If it's base64:
        //     this.imgURI = "data:image/jpeg;base64," + imageData;
        // }, (err) => {
        // });

        // capture callback
        var captureSuccess = function(mediaFiles) {
            var i, path, len;
            for (i = 0, len = mediaFiles.length; i < len; i += 1) {
                path = mediaFiles[i].fullPath;
                // do something interesting with the file
            }
        };

        // capture error callback
        var captureError = function(error) {
            navigator.notification.alert('Error code: ' + error.code, null, 'Capture Error');
        };

        var cordovaCapture = navigator.device.capture;
        cordovaCapture.captureVideo(captureSuccess, captureError, {limit:1}).then(function(videoData) {
            // Success! Video data is here
        }, function(err) {
            // An error occurred. Show a message to the user
        });
    }


}
