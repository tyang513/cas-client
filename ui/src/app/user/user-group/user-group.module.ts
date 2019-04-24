import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {NgZorroAntdModule} from 'ng-zorro-antd';
import {RouterModule} from '@angular/router';

import {UserGroupComponent} from './user-group.component';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        NgZorroAntdModule,
        RouterModule.forChild([{
            path: '',
            component: UserGroupComponent
        }]),
    ],
    declarations: [
        UserGroupComponent,
    ]
})
export class UserGroupModule {
}
