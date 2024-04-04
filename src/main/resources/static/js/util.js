function isEmpty(str){
    if(str != null && str.toString().length>0) {
        return false;
    } else {
        return true;
    }
}

function checkByteSize(str, min, max){
    if(str == null) return false;
    if(str.toString().length>=min && str.toString().length<=max){
        return true;
    }else{
        return false;
    }
}
function typeOf( obj ) {
    return ({}).toString.call( obj ).match(/\s(\w+)/)[1].toLowerCase();
}