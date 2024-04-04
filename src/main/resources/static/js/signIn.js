let param = {
    id: "",
    pw: ""
}

function setParam(){
    param.id = document.getElementById('id');
    param.pw = document.getElementById('pw');
}
function validateParam(){
    let isValidate = true;
    if(isEmpty(param.id)){
        document.getElementById('idMessage').textContent='* ID를 입력하세요.';
        isValidate = false;
    } else if(!checkByteSize(param.id,0,100)){
        document.getElementById('idMessage').textContent = '* ID 길이를 100byte 이하로 만들어주세요.'
        isValidate = false;
    }

    //pw 검증
    if(isEmpty(param.pw)){
        document.getElementById('pwMessage').textContent = '* 비밀번호를 입력해주세요.';
        isValidate = false;
    }else if(!checkByteSize(param.pw,0,100)){
        document.getElementById('pwMessage').textContent = '* 비밀번호 길이를 100byte 이하로 만들어 주세요.'
        isValidate = false;
    }
    return isValidate;
}
function eventBinding(){
    $("#signUpBtn").click(function(){
       return $.ajax({
           method: 'POST',
           url: '/signUpPage'
       });
    });

    $("#signInBtn").click(function(){
        setParam();
        if(!validateParam()) return;
        return $.ajax({
            method: 'POST',
            url: '/signIn',
            data: JSON.stringify(param),
            contentType: 'application/json; charset=UTF-8'
        });
    });
}

function init(){
    eventBinding();
}

$(document).ready(function(){
    init();
});