import {Component} from '@angular/core';
import {Loading, NavController,Storage, LocalStorage, Alert,MenuController} from 'ionic-angular';
import {HttpService} from '../../services/http.service';
import {CONFIG} from '../../config/config';
import {IntroducePage} from '../introduce/introduce';
import {Nav} from 'ionic-angular';
import {ViewChild} from '@angular/core';

@Component({
    templateUrl: 'build/pages/login/login.html'
})
export class LoginPage {
    user:string;
    error:string;
    code:string;
    local:Storage = new Storage(LocalStorage);

    constructor(private httpService:HttpService, private nav:NavController, private menu: MenuController) {

    }


    login() {

        var loading = this.startLoading();
        //callapi
        return this.httpService.get(CONFIG.SERVER_URL_SYSTEM)
            .then(
                res=> {
                    
                    return this.httpService.get(CONFIG.SERVER_URL + res.json()._links.logged_in_user.href)
                        .then(
                            res=>{
                                this.endLoading(loading);
                                this.local.set('user', res.text());
                                this.local.set('code', 123456);
                                this.openPage(IntroducePage);
                            }
                        )
                        .catch(
                            error=>{
                                this.endLoading(loading);
                                let alert = Alert.create({
                                    title: 'Login fail!',
                                    message: error.message,
                                    buttons: ['Ok']
                                });
                                this.nav.present(alert);
                            }
                        );
                }
            )
            .catch(
                error => {
                    this.endLoading(loading);
                    //alert message
                    let alert = Alert.create({
                        title: 'Login failed',
                        message: error.message,
                        buttons: ['Ok']
                    });
                    this.nav.present(alert);
                }
            );
    }

    openPage(page) {
        // close the menu when clicking a link from the menu
        this.menu.close();
        // navigate to the new page if it is not the current page
        this.nav.setRoot(page);
    }
    startLoading(){
        //loading
        let loading = Loading.create({
            dismissOnPageChange: true
        });
        this.nav.present(loading);
        return loading;
    }
    endLoading(loading){
        loading.dismiss();
    }
}
