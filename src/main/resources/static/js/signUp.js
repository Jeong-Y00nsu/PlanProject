let param={
    id: "",
    pw: "",
    repeatPw: "",
    name: ""
};

let flag = false;

function setParam(){
    param.id = document.getElementById('id').value;
    param.pw = document.getElementById('pw').value;
    param.name = document.getElementById('name').value;
    param.repeatPw = document.getElementById('repeatPw').value;
}
function validateParam(){
    if(!flag) {
        document.getElementById('idMessage').textContent = '* 중복 체크를 해주세요.';
        return false;
    }
    //id 검증
    if(isEmpty(param.id)){
        document.getElementById('idMessage').textContent = '* ID를 입력해주세요.';
        return false;
    }else if(!checkByteSize(param.id,0,100)){
        document.getElementById('idMessage').textContent = '* ID 길이를 100byte 이하로 만들어주세요.'
        return false;
    }
    //pw 검증
    if(isEmpty(param.pw)){
        document.getElementById('pwMessage').textContent = '* 비밀번호를 입력해주세요.';
        return false;
    }else if(!checkByteSize(param.pw,0,100)){
        document.getElementById('pwMessage').textContent = '* 비밀번호 길이를 100byte 이하로 만들어 주세요.'
        return false;
    }
    if(isEmpty(param.repeatPw)){
        document.getElementById('pwMessage').textContent = '* 비밀번호를 한 번 더 입력해주세요.'
        return false;
    }else if(!checkByteSize(param.repeatPw,0,100)){
        document.getElementById('pwMessage').textContent = '* 재입력한 비밀번호 길이를 100byte 이하로 만들어 주세요.'
        return false;
    }

    if(param.pw !== param.repeatPw){
        document.getElementById('pwMessage').textContent = '* 재입력한 비밀번호가 비밀번호와 일치하지 않습니다.'
        return false;
    }
    //name 검증
    if(isEmpty(param.name)){
        document.getElementById('nameMessage').textContent = '* 닉네임을 입력해주세요.';
        return false;
    } else if(!checkByteSize(param.name,0,100)){
        document.getElementById('nameMessage').textContent = '* 닉네임 길이를 100byte 이하로 만들어주세요.';
        return false;
    }
    return true;
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

    $("#checkDuplicateId").click(function(){
        var parameter = document.getElementById('id').value;
        console.log("Sending ID:", parameter); // 이 로그를 통해 전송되는 데이터 확인
        flag = true;
        return $.ajax({
            method: 'POST',
            url: '/checkDuplicateId',
            contentType: 'application/json',
            data: JSON.stringify({ id: parameter }),
            dataType: 'json',
            success: function(response){
                if(response.result !== "OK"){
                    flag = false;
                }
                $("#idMessage").text(response.message);
            },
            error: function (){
                alert('Error check duplicate id');
            }
        });
    });
}
function init() {
    eventBinding();
}

$(document).ready(function(){
    init();
});