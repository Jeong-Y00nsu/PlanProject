function validateParam(param){
    let isValidate = true;
    if(isEmpty(param.title)){
        document.getElementById('message').textContent = '* 일정 제목을 입력하세요.';
        isValidate = false;
    } else if(!checkByteSize(param.title,0,100)){
        document.getElementById('message').textContent = '* 일정 제목 길이를 100byte 이하로 작성해주세요.';
        isValidate = false;
    }

    if(isEmpty(param.text)){
        document.getElementById('message').textContent = '* 일정 내용을 입력하세요.';
        isValidate = false;
    } else if(!checkByteSize(param.text,0,60000)){
        document.getElementById('message').textContent = '* 일정 내용 길이를 60000byte 이하로 작성해주세요.';
        isValidate = false;
    }

    if(param.startDt == null){
        document.getElementById('message').textContent = '* 일정의 시작일을 정해주세요.';
        isValidate = false;
    }

    if(param.endDt == null){
        document.getElementById('message').textContent = '* 일정 종료일을 입력하세요.';
        isValidate = false;
    }

    if(utils.compare(param.startDt, param.endDt)>=0){
        document.getElementById('message').textContent = '* 일정 종료일은 시작일보다 미래 시점이어야 합니다.';
        isValidate = false;
    }
    return isValidate;
}

function eventBinding(){
    $('#addPlanBtn').click(function(){
        let param = {
            title: document.getElementById('title').value,
            text: document.getElementById('text').value,
            startDt: document.getElementById('startDt').value,
            endDt: document.getElementById('endDt').value
        }
        if(!validateParam(param)) return;
        return $.ajax({
            method: 'POST',
            url: '/addPlan',
            data: JSON.stringify(param),
            contentType: 'application/json; charset=UTF-8'
        });
    });

    $('cancelBtn').click(function(){
        let param ={
            year: document.getElementById('year').value,
            month: document.getElementById('month').value,
            day: document.getElementById('day').value
        }
        return $.ajax({
            method: 'POST',
            url: '/getDailyPlan',
            data: JSON.stringify(param),
            contentType: 'application/json; charset=UTF-8'
        });
    });

}

function init(){
    eventBinding();
    var datepicker = new tui.DatePicker('#wrapper', {
        date: new Date(),
        input: {
            element: '#startDt',
            format: 'yyyy-MM-dd HH:mm A'
        },
        timePicker: {
            layoutType: 'tab',
            inputType: 'spinbox'
        }
    });

    var datepicker2 = new tui.DatePicker('#wrapper-2', {
        date: new Date(),
        input: {
            element: '#endDt',
            format: 'yyyy-MM-dd HH:mm A'
        },
        timePicker: {
            layoutType: 'tab',
            inputType: 'spinbox'
        }
    });

}

$(document).ready(function(){
    init();
});