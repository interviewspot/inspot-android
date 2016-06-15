import {Injectable}    from '@angular/core';
import {Headers, Http, RequestOptions} from '@angular/http';
import {CONFIG} from '../config/config';

import 'rxjs/add/operator/toPromise';


@Injectable()
export class HttpService {

    constructor(private http:Http) {
    }

    get(url:string):Promise<any> {

        //header
        let headers = new Headers();
        headers.append('Content-Type', 'application/json');
        headers.append('x-mode', 'inv_code');
        headers.append('x-username', '123456');
        headers.append('x-password', '');
        let options = new RequestOptions({headers: headers});

        return this.http.get(url, options)
            .toPromise()
            .then(
                res =>res
            )
            .catch(
                error =>{
                    if(error.status ===401){
                        throw new Error('Unauthorised Operation !')
                    }
                }
            );
    }
}


/*
 Copyright 2016 Google Inc. All Rights Reserved.
 Use of this source code is governed by an MIT-style license that
 can be found in the LICENSE file at http://angular.io/license
 */