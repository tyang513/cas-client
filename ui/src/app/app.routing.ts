import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

const appRoutes: Routes = [
    {
        path: '',
        redirectTo: 'cas-client',
        pathMatch: 'full'
    },
    // ==========================================
    {
        // 用户-用户分群
        path: 'cas-client',
        loadChildren: './user/user-group/user-group.module#UserGroupModule'
    },
];

@NgModule({
    imports: [
        RouterModule.forRoot(
            appRoutes,
            {useHash: true}
        )
    ],
    exports: [
        RouterModule
    ]
})

export class AppRoutingModule {

}
