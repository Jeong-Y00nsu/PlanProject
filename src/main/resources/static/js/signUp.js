let param={
    id: "",
    pw: "",
    repeatPw: "",
    name: ""
};

function setParam(){
    param.id = document.getElementById('id');
    param.pw = document.getElementById('pw');
    param.name = document.getElementById('name');
}
function validateParam(){
    let isValidate = true;
    //id 검증
    if(isEmpty(param.id)){
        document.getElementById('idMessage').textContent = '* ID를 입력해주세요.';
        isValidate = false;
    }else if(!checkByteSize(param.id,0,100)){
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
    if(isEmpty(param.repeatPw)){
        document.getElementById('pwMessage').textContent = '* 비밀번호를 한 번 더 입력해주세요.'
        isValidate = false;
    }else if(!checkByteSize(param.repeatPw,0,100)){
        document.getElementById('pwMessage').textContent = '* 재입력한 비밀번호 길이를 100byte 이하로 만들어 주세요.'
        isValidate = false;
    }

    if(param.pw !== param.repeatPw){
        document.getElementById('pwMessage').textContent = '* 재입력한 비밀번호가 비밀번호와 일치하지 않습니다.'
        isValidate = false;
    }
    //name 검증
    if(isEmpty(param.name)){
        document.getElementById('nameMessage').textContent = '* 닉네임을 입력해주세요.';
        isValidate = false;
    } else if(!checkByteSize(param.name,0,100)){
        document.getElementById('nameMessage').textContent = '* 닉네임 길이를 100byte 이하로 만들어주세요.';
        isValidate = false;
    }
    return isValidate;
}

function eventBinding() {
    $("#signUpBtn").click(function(){
        setParam();
        if(!validateParam()) return;
        return $.ajax({
            method: 'POST',
            url: '/signUp',
            data: JSON.stringify(param),
            contentType: 'application/json; charset=UTF-8'
        });
    });
}
function init() {
    eventBinding();
}

$(document).ready(function(){
    init();
});