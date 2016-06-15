import {Component} from '@angular/core';
import {NavController, Storage, LocalStorage, Loading, Alert} from 'ionic-angular';
import {HttpService} from '../../services/http.service';
import {CONFIG} from '../../config/config';
import {ControlPage} from '../control/control';

/*
 Generated class for the IntroducePage page.

 See http://ionicframework.com/docs/v2/components/#navigation for more info on
 Ionic pages and navigation.
 */
@Component({
    templateUrl: 'build/pages/introduce/introduce.html',
})
export class IntroducePage {
    local:Storage = new Storage(LocalStorage);
    user:any;
    candidate = {
        'deadline': '',
        'role': '',
        'company': '',
        'responsibility': ''
    };

    constructor(public nav:NavController, private httpService:HttpService) {
        this.local.get('user').then(
            user=> {


                this.user = JSON.parse(user);
                this.local.get('code')
                    .then(
                        code => {
                            var loading = this.startLoading();
                            this.httpService.get(this.user._links.job_candidates.href + '?search=invitation.code:' + code)
                                .then(
                                    res=> {
                                        var data = res.json();
                                        this.candidate.deadline = data._embedded.items[0].deadline;
                                        this.httpService.get(data._embedded.items[0]._links.job_listing.href)
                                            .then(
                                                res => {
                                                    var jobListing = res.json();
                                                    this.candidate.role = jobListing.title;
                                                    this.candidate.responsibility = jobListing.description;
                                                    this.httpService.get(jobListing._links.organisation.href)
                                                        .then(
                                                            res => {
                                                                this.candidate.company = res.json().name;
                                                                //stop loading
                                                                this.endLoading(loading);

                                                            }
                                                        )
                                                        .catch(
                                                            error=> {
                                                                //stop loading
                                                                this.endLoading(loading);
                                                                //alert message
                                                                this.alert(error.message);
                                                            }
                                                        )

                                                }
                                            )
                                            .catch(
                                                error=> {
                                                    //stop loading
                                                    this.endLoading(loading);
                                                    //alert message
                                                    this.alert(error.message);
                                                }
                                            )
                                    }
                                )
                                .catch(
                                    error=> {
                                        //stop loading
                                        // this.endLoading(loading);
                                        //alert message
                                        this.alert(error.message);
                                    }
                                );
                        }
                    )
            }
        )
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

    next() {
        this.nav.setRoot(ControlPage);
    }


}
