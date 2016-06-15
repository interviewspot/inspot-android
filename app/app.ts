import {Component, ViewChild} from '@angular/core';
import {ionicBootstrap ,Platform, MenuController, Nav} from 'ionic-angular';
import {StatusBar} from 'ionic-native';
import {IntroducePage} from './pages/introduce/introduce';
import {LoginPage} from './pages/login/login';
import {ListPage} from './pages/list/list';
import {HttpService} from './services/http.service';
import {VideoIntroducePage} from './pages/video-introduce/video-introduce';




@Component({
  templateUrl: 'build/app.html',
})
class MyApp {
  @ViewChild(Nav)   nav: Nav;

  // make HelloIonicPage the root (or first) page
  rootPage: any = LoginPage;
  pages: Array<{title: string, component: any}>;

  constructor(
    private platform: Platform,
    private menu: MenuController
  ) {
    this.initializeApp();

    // set our app's pages
    this.pages = [
      { title: 'My First List', component: ListPage },
      { title: 'Introduction Page', component: IntroducePage },
      { title: 'Video Introduce Page', component: VideoIntroducePage }
    ];
  }

  initializeApp() {
    this.platform.ready().then(() => {
      // Okay, so the platform is ready and our plugins are available.
      // Here you can do any higher level native things you might need.
      // StatusBar.styleDefault();
    });
  }

  openPage(page) {
    // close the menu when clicking a link from the menu
    this.menu.close();
    // navigate to the new page if it is not the current page
    this.nav.setRoot(page.component);
  }
}

ionicBootstrap(MyApp, [HttpService], {
  // tabbarPlacement: 'bottom'
});
