(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-eaa69404"],{"28a5":function(t,e,o){"use strict";var i=o("aae3"),s=o("cb7c"),r=o("ebd6"),n=o("0390"),a=o("9def"),c=o("5f1b"),l=o("520a"),u=o("79e5"),m=Math.min,p=[].push,h="split",d="length",f="lastIndex",g=4294967295,v=!u((function(){RegExp(g,"y")}));o("214f")("split",2,(function(t,e,o,u){var w;return w="c"=="abbc"[h](/(b)*/)[1]||4!="test"[h](/(?:)/,-1)[d]||2!="ab"[h](/(?:ab)*/)[d]||4!="."[h](/(.?)(.?)/)[d]||"."[h](/()()/)[d]>1||""[h](/.?/)[d]?function(t,e){var s=String(this);if(void 0===t&&0===e)return[];if(!i(t))return o.call(s,t,e);var r,n,a,c=[],u=(t.ignoreCase?"i":"")+(t.multiline?"m":"")+(t.unicode?"u":"")+(t.sticky?"y":""),m=0,h=void 0===e?g:e>>>0,v=new RegExp(t.source,u+"g");while(r=l.call(v,s)){if(n=v[f],n>m&&(c.push(s.slice(m,r.index)),r[d]>1&&r.index<s[d]&&p.apply(c,r.slice(1)),a=r[0][d],m=n,c[d]>=h))break;v[f]===r.index&&v[f]++}return m===s[d]?!a&&v.test("")||c.push(""):c.push(s.slice(m)),c[d]>h?c.slice(0,h):c}:"0"[h](void 0,0)[d]?function(t,e){return void 0===t&&0===e?[]:o.call(this,t,e)}:o,[function(o,i){var s=t(this),r=void 0==o?void 0:o[e];return void 0!==r?r.call(o,s,i):w.call(String(s),o,i)},function(t,e){var i=u(w,t,this,e,w!==o);if(i.done)return i.value;var l=s(t),p=String(this),h=r(l,RegExp),d=l.unicode,f=(l.ignoreCase?"i":"")+(l.multiline?"m":"")+(l.unicode?"u":"")+(v?"y":"g"),b=new h(v?l:"^(?:"+l.source+")",f),y=void 0===e?g:e>>>0;if(0===y)return[];if(0===p.length)return null===c(b,p)?[p]:[];var x=0,k=0,_=[];while(k<p.length){b.lastIndex=v?k:0;var F,E=c(b,v?p:p.slice(k));if(null===E||(F=m(a(b.lastIndex+(v?0:k)),p.length))===x)k=n(p,k,d);else{if(_.push(p.slice(x,k)),_.length===y)return _;for(var R=1;R<=E.length-1;R++)if(_.push(E[R]),_.length===y)return _;k=x=F}}return _.push(p.slice(x)),_}]}))},"3e10":function(t,e,o){"use strict";o.d(e,"b",(function(){return s})),o.d(e,"a",(function(){return r})),o.d(e,"c",(function(){return n}));o("28a5");var i={"qq.com":"http://mail.qq.com","gmail.com":"http://mail.google.com","sina.com":"http://mail.sina.com.cn","163.com":"http://mail.163.com","126.com":"http://mail.126.com","yeah.net":"http://www.yeah.net/","sohu.com":"http://mail.sohu.com/","tom.com":"http://mail.tom.com/","sogou.com":"http://mail.sogou.com/","139.com":"http://mail.10086.cn/","hotmail.com":"http://www.hotmail.com","live.com":"http://login.live.com/","live.cn":"http://login.live.cn/","live.com.cn":"http://login.live.com.cn","189.com":"http://webmail16.189.cn/webmail/","yahoo.com.cn":"http://mail.cn.yahoo.com/","yahoo.cn":"http://mail.cn.yahoo.com/","eyou.com":"http://www.eyou.com/","21cn.com":"http://mail.21cn.com/","188.com":"http://www.188.com/","dingtalk.com":"https://mail.dingtalk.com/","outlook.com":"https://outlook.live.com/","foxmail.com":"http://www.foxmail.com"};function s(t,e){n(t,(function(t){e(t)}))}function r(t){if(t&&t.indexOf("@")>-1){var e="",o=t.split("@"),i=o[0];return e=i.length<=3?"".concat(i.substring(0,1),"***@").concat(o[1]):"".concat(i.substring(0,3),"***@").concat(o[1]),e}return""}function n(t,e){try{var o=t.split("@"),i=o[1],s=a(i);e(s)}catch(r){console.log("解析邮箱失败, email:"+t,r)}}function a(t){return i[t]}},"546d":function(t,e,o){"use strict";o("7679")},"6aad":function(t,e,o){},7679:function(t,e,o){},"8ff7":function(t,e,o){"use strict";o.r(e);var i=function(){var t=this,e=t.$createElement,o=t._self._c||e;return o("div",{staticClass:"app-container"},[o("el-form",{directives:[{name:"show",rawName:"v-show",value:!t.submited,expression:"!submited"}],ref:"regForm",staticClass:"center-form",attrs:{model:t.regForm,rules:t.regRules},nativeOn:{submit:function(t){t.preventDefault()}}},[o("h3",[t._v("接入方注册")]),t._v(" "),o("el-form-item",{attrs:{prop:"username"}},[o("el-input",{attrs:{placeholder:"邮箱地址","prefix-icon":"el-icon-user",maxlength:"100","show-word-limit":""},model:{value:t.regForm.username,callback:function(e){t.$set(t.regForm,"username",e)},expression:"regForm.username"}})],1),t._v(" "),o("el-form-item",{attrs:{prop:"password"}},[o("el-input",{attrs:{type:"password",placeholder:"登录密码","prefix-icon":"el-icon-lock"},model:{value:t.regForm.password,callback:function(e){t.$set(t.regForm,"password",e)},expression:"regForm.password"}})],1),t._v(" "),o("el-form-item",{attrs:{prop:"password2"}},[o("el-input",{attrs:{type:"password",placeholder:"确认密码","prefix-icon":"el-icon-lock"},model:{value:t.regForm.password2,callback:function(e){t.$set(t.regForm,"password2",e)},expression:"regForm.password2"}})],1),t._v(" "),o("el-button",{staticStyle:{width:"100%"},attrs:{type:"primary","native-type":"submit"},on:{click:t.handleReg}},[t._v("注 册")]),t._v(" "),o("div",{staticClass:"footer"},[t._v("\n      已有账号，"),o("el-button",{attrs:{type:"text"},on:{click:t.goLogin}},[t._v("去登录")])],1)],1),t._v(" "),o("div",{directives:[{name:"show",rawName:"v-show",value:t.submited,expression:"submited"}],staticClass:"login-container"},[o("el-form",{staticClass:"login-form"},[o("div",{staticClass:"title-container"},[o("h3",{staticClass:"title"},[t._v("账号激活")])]),t._v(" "),o("el-alert",{staticClass:"el-alert-tip",attrs:{closable:!1}},[o("div",{attrs:{slot:"title"},slot:"title"},[t._v("\n          我们向邮箱 "+t._s(t.formatEmail())+" 发送了一封含有账号激活链接的邮件。请登录邮箱查看，如长时间没有收到邮件，请检查你的垃圾邮件文件夹。\n        ")])]),t._v(" "),o("el-button",{directives:[{name:"show",rawName:"v-show",value:t.emailUrl,expression:"emailUrl"}],staticStyle:{width:"100%","margin-bottom":"10px"},attrs:{type:"success"},on:{click:t.goEmailPage}},[t._v("前往登录邮箱")]),t._v(" "),o("br"),t._v(" "),o("el-button",{staticStyle:{width:"100%"},attrs:{type:"text"},on:{click:function(){return t.goRoute("/login")}}},[t._v("前往登录页")])],1)],1)],1)},s=[],r=(o("7f7f"),o("8237")),n=o.n(r),a=o("3e10"),c={name:"RegIsv",data:function(){var t=this,e=function(e,o,i){o!==t.regForm.password?i(new Error("两次密码不一致")):i()};return{query:{},submited:!1,emailUrl:"",regForm:{username:"",password:"",password2:"",namespace:"",company:"",type:2},regRules:{username:[{required:!0,message:"请填写邮箱地址",trigger:"blur"},{type:"email",message:"请输入正确的邮箱地址",trigger:["blur"]}],password:[{required:!0,message:"请输入密码",trigger:"blur"},{min:6,message:"密码长度不能小于6位",trigger:"blur"}],password2:[{required:!0,trigger:"blur",validator:e}]},loading:!1,passwordType:"password",password2Type:"password",regTitle:"接入方注册"}},created:function(){},methods:{showPwd:function(){var t=this;"password"===this.passwordType?this.passwordType="":this.passwordType="password",this.$nextTick((function(){t.$refs.password.focus()}))},showPwd2:function(){var t=this;"password"===this.password2Type?this.password2Type="":this.password2Type="password",this.$nextTick((function(){t.$refs.password2.focus()}))},onTabClick:function(t){this.$router.push({path:"/".concat(t.name,"Reg")})},goLogin:function(){this.goRoute("/login")},handleReg:function(){var t=this;this.$refs.regForm.validate((function(e){e&&t.doSubmit()}))},parseEmailUrl:function(){var t=this;Object(a["b"])(this.regForm.username,(function(e){t.emailUrl=e}))},goEmailPage:function(){this.emailUrl&&window.open(this.emailUrl)},formatEmail:function(){return Object(a["a"])(this.regForm.username)},onCaptchaSuccess:function(t){this.doSubmit((function(e){e.captcha=t}))},doSubmit:function(t){var e=this,o={};Object.assign(o,this.regForm),o.password=n()(o.password),t&&t.call(this,o),this.parseEmailUrl(),this.post("/portal/common/regIsv",o,(function(t){o.needVerifyEmail?this.submited=!0:this.alert("注册成功","提示",(function(){this.goRoute("/login")}))}),(function(t){e.tipError(t.msg)}))},useVerify:function(){this.$refs.verify.show()}}},l=c,u=(o("546d"),o("bdb7"),o("2877")),m=Object(u["a"])(l,i,s,!1,null,"6695e9ee",null);e["default"]=m.exports},aae3:function(t,e,o){var i=o("d3f4"),s=o("2d95"),r=o("2b4c")("match");t.exports=function(t){var e;return i(t)&&(void 0!==(e=t[r])?!!e:"RegExp"==s(t))}},bdb7:function(t,e,o){"use strict";o("6aad")}}]);