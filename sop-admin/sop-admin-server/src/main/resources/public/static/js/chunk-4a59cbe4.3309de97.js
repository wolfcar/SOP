(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-4a59cbe4"],{"02f4":function(t,e,r){var n=r("4588"),i=r("be13");t.exports=function(t){return function(e,r){var o,a,l=String(i(e)),c=n(r),s=l.length;return c<0||c>=s?t?"":void 0:(o=l.charCodeAt(c),o<55296||o>56319||c+1===s||(a=l.charCodeAt(c+1))<56320||a>57343?t?l.charAt(c):o:t?l.slice(c,c+2):a-56320+(o-55296<<10)+65536)}}},"0390":function(t,e,r){"use strict";var n=r("02f4")(!0);t.exports=function(t,e,r){return e+(r?n(t,e).length:1)}},"0a49":function(t,e,r){var n=r("9b43"),i=r("626a"),o=r("4bf8"),a=r("9def"),l=r("cd1c");t.exports=function(t,e){var r=1==t,c=2==t,s=3==t,u=4==t,f=6==t,d=5==t||f,v=e||l;return function(e,l,p){for(var g,h,b=o(e),m=i(b),D=n(l,p,3),x=a(m.length),y=0,_=r?v(e,x):c?v(e,0):void 0;x>y;y++)if((d||y in m)&&(g=m[y],h=D(g,y,b),t))if(r)_[y]=h;else if(h)switch(t){case 3:return!0;case 5:return g;case 6:return y;case 2:_.push(g)}else if(u)return!1;return f?-1:s||u?u:_}}},"0bfb":function(t,e,r){"use strict";var n=r("cb7c");t.exports=function(){var t=n(this),e="";return t.global&&(e+="g"),t.ignoreCase&&(e+="i"),t.multiline&&(e+="m"),t.unicode&&(e+="u"),t.sticky&&(e+="y"),e}},1169:function(t,e,r){var n=r("2d95");t.exports=Array.isArray||function(t){return"Array"==n(t)}},"20d6":function(t,e,r){"use strict";var n=r("5ca1"),i=r("0a49")(6),o="findIndex",a=!0;o in[]&&Array(1)[o](function(){a=!1}),n(n.P+n.F*a,"Array",{findIndex:function(t){return i(this,t,arguments.length>1?arguments[1]:void 0)}}),r("9c6c")(o)},"214f":function(t,e,r){"use strict";r("b0c5");var n=r("2aba"),i=r("32e9"),o=r("79e5"),a=r("be13"),l=r("2b4c"),c=r("520a"),s=l("species"),u=!o(function(){var t=/./;return t.exec=function(){var t=[];return t.groups={a:"7"},t},"7"!=="".replace(t,"$<a>")}),f=function(){var t=/(?:)/,e=t.exec;t.exec=function(){return e.apply(this,arguments)};var r="ab".split(t);return 2===r.length&&"a"===r[0]&&"b"===r[1]}();t.exports=function(t,e,r){var d=l(t),v=!o(function(){var e={};return e[d]=function(){return 7},7!=""[t](e)}),p=v?!o(function(){var e=!1,r=/a/;return r.exec=function(){return e=!0,null},"split"===t&&(r.constructor={},r.constructor[s]=function(){return r}),r[d](""),!e}):void 0;if(!v||!p||"replace"===t&&!u||"split"===t&&!f){var g=/./[d],h=r(a,d,""[t],function(t,e,r,n,i){return e.exec===c?v&&!i?{done:!0,value:g.call(e,r,n)}:{done:!0,value:t.call(r,e,n)}:{done:!1}}),b=h[0],m=h[1];n(String.prototype,t,b),i(RegExp.prototype,d,2==e?function(t,e){return m.call(t,this,e)}:function(t){return m.call(t,this)})}}},"520a":function(t,e,r){"use strict";var n=r("0bfb"),i=RegExp.prototype.exec,o=String.prototype.replace,a=i,l="lastIndex",c=function(){var t=/a/,e=/b*/g;return i.call(t,"a"),i.call(e,"a"),0!==t[l]||0!==e[l]}(),s=void 0!==/()??/.exec("")[1],u=c||s;u&&(a=function(t){var e,r,a,u,f=this;return s&&(r=new RegExp("^"+f.source+"$(?!\\s)",n.call(f))),c&&(e=f[l]),a=i.call(f,t),c&&a&&(f[l]=f.global?a.index+a[0].length:e),s&&a&&a.length>1&&o.call(a[0],r,function(){for(u=1;u<arguments.length-2;u++)void 0===arguments[u]&&(a[u]=void 0)}),a}),t.exports=a},"5f1b":function(t,e,r){"use strict";var n=r("23c6"),i=RegExp.prototype.exec;t.exports=function(t,e){var r=t.exec;if("function"===typeof r){var o=r.call(t,e);if("object"!==typeof o)throw new TypeError("RegExp exec method returned something other than an Object or null");return o}if("RegExp"!==n(t))throw new TypeError("RegExp#exec called on incompatible receiver");return i.call(t,e)}},a481:function(t,e,r){"use strict";var n=r("cb7c"),i=r("4bf8"),o=r("9def"),a=r("4588"),l=r("0390"),c=r("5f1b"),s=Math.max,u=Math.min,f=Math.floor,d=/\$([$&`']|\d\d?|<[^>]*>)/g,v=/\$([$&`']|\d\d?)/g,p=function(t){return void 0===t?t:String(t)};r("214f")("replace",2,function(t,e,r,g){return[function(n,i){var o=t(this),a=void 0==n?void 0:n[e];return void 0!==a?a.call(n,o,i):r.call(String(o),n,i)},function(t,e){var i=g(r,t,this,e);if(i.done)return i.value;var f=n(t),d=String(this),v="function"===typeof e;v||(e=String(e));var b=f.global;if(b){var m=f.unicode;f.lastIndex=0}var D=[];while(1){var x=c(f,d);if(null===x)break;if(D.push(x),!b)break;var y=String(x[0]);""===y&&(f.lastIndex=l(d,o(f.lastIndex),m))}for(var _="",w=0,I=0;I<D.length;I++){x=D[I];for(var S=String(x[0]),k=s(u(a(x.index),d.length),0),A=[],E=1;E<x.length;E++)A.push(p(x[E]));var M=x.groups;if(v){var F=[S].concat(A,k,d);void 0!==M&&F.push(M);var L=String(e.apply(void 0,F))}else L=h(S,d,k,A,M,e);k>=w&&(_+=d.slice(w,k)+L,w=k+S.length)}return _+d.slice(w)}];function h(t,e,n,o,a,l){var c=n+t.length,s=o.length,u=v;return void 0!==a&&(a=i(a),u=d),r.call(l,u,function(r,i){var l;switch(i.charAt(0)){case"$":return"$";case"&":return t;case"`":return e.slice(0,n);case"'":return e.slice(c);case"<":l=a[i.slice(1,-1)];break;default:var u=+i;if(0===u)return r;if(u>s){var d=f(u/10);return 0===d?r:d<=s?void 0===o[d-1]?i.charAt(1):o[d-1]+i.charAt(1):r}l=o[u-1]}return void 0===l?"":l})}})},b0c5:function(t,e,r){"use strict";var n=r("520a");r("5ca1")({target:"RegExp",proto:!0,forced:n!==/./.exec},{exec:n})},cd1c:function(t,e,r){var n=r("e853");t.exports=function(t,e){return new(n(t))(e)}},e853:function(t,e,r){var n=r("d3f4"),i=r("1169"),o=r("2b4c")("species");t.exports=function(t){var e;return i(t)&&(e=t.constructor,"function"!=typeof e||e!==Array&&!i(e.prototype)||(e=void 0),n(e)&&(e=e[o],null===e&&(e=void 0))),void 0===e?Array:e}},ec2a:function(t,e,r){"use strict";r.r(e);var n=function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("div",{staticClass:"app-container"},[r("el-form",{staticClass:"demo-form-inline",attrs:{inline:!0,model:t.searchFormData,size:"mini"}},[r("el-form-item",[r("el-button",{attrs:{type:"primary",icon:"el-icon-plus"},on:{click:t.onAddServer}},[t._v("添加监控服务器")])],1)],1),t._v(" "),r("el-table",{staticStyle:{width:"100%","margin-bottom":"20px"},attrs:{data:t.tableData,border:"","row-key":"treeId","empty-text":"请添加监控服务器"}},[r("el-table-column",{attrs:{prop:"monitorName",label:"网关实例",width:"300"},scopedSlots:t._u([{key:"default",fn:function(e){return[0===e.row.parentId?r("span",[t._v(t._s(e.row.monitorName))]):t._e()]}}])}),t._v(" "),r("el-table-column",{attrs:{prop:"serviceId",label:"serviceId",width:"200"},scopedSlots:t._u([{key:"default",fn:function(e){return[e.row.parentId>0?r("span",[t._v(t._s(e.row.serviceId))]):t._e()]}}])}),t._v(" "),r("el-table-column",{attrs:{prop:"name",label:"接口名 (版本号)",width:"200"},scopedSlots:t._u([{key:"default",fn:function(e){return[t._v("\n        "+t._s(e.row.name+(e.row.version?" ("+e.row.version+")":""))+"\n      ")]}}])}),t._v(" "),r("el-table-column",{attrs:{prop:"count",label:"出错次数",width:"100"}}),t._v(" "),r("el-table-column",{attrs:{prop:"errorMsg",label:"报错信息",width:"300"},scopedSlots:t._u([{key:"default",fn:function(e){return e.row.parentId>0?[r("div",{staticStyle:{display:"inline-block"},domProps:{innerHTML:t._s(t.showErrorMsg(e.row))}}),t._v(" "),r("el-button",{attrs:{type:"text",size:"mini"},on:{click:function(r){return t.onShowErrorDetail(e.row)}}},[t._v("详情")])]:void 0}}],null,!0)}),t._v(" "),r("el-table-column",{attrs:{label:"操作",width:"180"},scopedSlots:t._u([{key:"default",fn:function(e){return[0===e.row.parentId&&e.row.children?r("el-button",{attrs:{type:"text",size:"mini"},on:{click:function(r){return t.onClearLog(e.row)}}},[t._v("清空日志")]):t._e(),t._v(" "),0===e.row.parentId?r("el-button",{attrs:{type:"text",size:"mini"},on:{click:function(r){return t.onDelete(e.row)}}},[t._v("删除实例")]):t._e()]}}])})],1),t._v(" "),r("el-dialog",{attrs:{title:"选择服务器实例",visible:t.logDialogInstanceVisible,"close-on-click-modal":!1},on:{"update:visible":function(e){t.logDialogInstanceVisible=e}}},[r("el-form",{ref:"logDialogForm",attrs:{model:t.logDialogFormData,rules:t.rulesLog,"label-width":"150px",size:"mini"}},[r("el-form-item",[r("p",{staticStyle:{color:"#878787"}},[t._v("只能选择网关实例，其它实例不支持")])]),t._v(" "),r("el-form-item",{attrs:{prop:"instanceData",label:"服务器实例"}},[r("el-select",{staticStyle:{width:"400px"},attrs:{"value-key":"id"},model:{value:t.logDialogFormData.instanceData,callback:function(e){t.$set(t.logDialogFormData,"instanceData",e)},expression:"logDialogFormData.instanceData"}},t._l(t.serviceData,function(e){return r("el-option",{key:e.id,attrs:{label:e.name+"("+e.ipAddr+":"+e.serverPort+")",value:e,disabled:t.isOptionDisabled(e)}},[r("span",{staticStyle:{float:"left"}},[t._v(t._s(e.name)+" "),t.isOptionDisabled(e)?r("span",[t._v("(已添加)")]):t._e()]),t._v(" "),r("span",{staticStyle:{float:"right",color:"#8492a6","font-size":"13px"}},[t._v(t._s(e.ipAddr+":"+e.serverPort))])])}),1)],1)],1),t._v(" "),r("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[r("el-button",{on:{click:function(e){t.logDialogInstanceVisible=!1}}},[t._v("取 消")]),t._v(" "),r("el-button",{attrs:{type:"primary"},on:{click:t.onLogDialogSave}},[t._v("保 存")])],1)],1),t._v(" "),r("el-dialog",{attrs:{title:"错误详情",visible:t.logDetailVisible,width:"60%"},on:{"update:visible":function(e){t.logDetailVisible=e}}},[r("div",{staticStyle:{"overflow-x":"auto"},domProps:{innerHTML:t._s(t.errorMsgDetail)}}),t._v(" "),r("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[r("el-button",{attrs:{type:"primary"},on:{click:function(e){t.logDetailVisible=!1}}},[t._v("关 闭")])],1)])],1)},i=[],o=(r("a481"),r("ac6a"),r("20d6"),{data:function(){return{searchFormData:{},tableData:[],serviceData:[],addedInstanceList:[],logDialogFormData:{instanceData:null},logDialogInstanceVisible:!1,logDetailVisible:!1,rulesLog:{instanceData:[{required:!0,message:"不能为空",trigger:"blur"}]},errorMsgDetail:""}},created:function(){this.loadServiceInstance(),this.loadTable()},methods:{loadServiceInstance:function(){this.post("service.instance.list",{},function(t){this.serviceData=t.data.filter(function(t){return t.instanceId&&t.instanceId.length>0})}),this.post("monitor.instance.list",{},function(t){this.addedInstanceList=t.data})},loadTable:function(){this.post("monitor.log.list",{},function(t){this.tableData=this.buildTreeData(t.data)})},isOptionDisabled:function(t){var e=t.ipAddr+":"+t.serverPort,r=this.addedInstanceList.findIndex(function(t,r,n){return t===e});return r>-1},buildTreeData:function(t){return t.forEach(function(e){var r=e.parentId;0===r||t.forEach(function(t){if(t.treeId===r){var n=t.children;n||(n=[]),n.push(e),t.children=n}})}),t=t.filter(function(t){return 0===t.parentId}),t},showErrorMsg:function(t){var e=t.errorMsg.replace(/\<br\>/g,"");return e.substring(0,30)+"..."},onAddServer:function(){this.logDialogInstanceVisible=!0},onDelete:function(t){this.confirm("确定要删除实例【"+t.monitorName+"】吗?",function(e){this.post("monitor.instance.del",{id:t.rawId},function(t){e(),this.tip("删除成功"),this.loadTable()})})},onClearLog:function(t){this.confirm("确定要清空日志吗?",function(e){this.post("monitor.log.clear",{id:t.rawId},function(t){e(),this.tip("清空成功"),this.loadTable()})})},onShowErrorDetail:function(t){this.errorMsgDetail=t.errorMsg,this.logDetailVisible=!0},onLogDialogSave:function(){var t=this;this.$refs["logDialogForm"].validate(function(e){if(e){var r=t.logDialogFormData.instanceData,n={serviceId:r.serviceId,ip:r.ipAddr,port:r.serverPort};t.post("monitor.instance.add",n,function(t){this.logDialogInstanceVisible=!1,this.loadTable()})}})}}}),a=o,l=r("2877"),c=Object(l["a"])(a,n,i,!1,null,null,null);e["default"]=c.exports}}]);