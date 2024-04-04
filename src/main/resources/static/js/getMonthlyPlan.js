let tr =
    `<tr>
        <th></th>
        <th></th>
        <th></th>
        <th></th>
        <th></th>
        <th></th>
        <th></th>
    </tr>
    <tr>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
    </tr>`

function displayMonthCalendar(planParam){
    let result = {
        year:0,
        month:0,
        cntOfWeeks: 0,
        firstDayOfWeek: 0,
        lastDay: 0,
        plans: []
    };

    let table = document.getElementById("calendar");
    result = planParam;

    for(var i=0;i<result.cntOfWeeks;i++){
        let tmpDiv = document.createElement('div');
        let tbody = document.createElement("tbody");
        tmpDiv.innerHTML = tr;

        let thElements = tmpDiv.querySelectorAll('tr:first-child th');
        let tdElements = tmpDiv.querySelectorAll('tr:last-child td');

        for(let j=0;j<7;j++){
            let day = 7*i+j-result.firstDayOfWeek+1;
            if(day > 0 && day <= result.lastDay){
                // <td> 안에 날짜 넣기
                thElements[j].textContent = day+'일';
                let currentMonthStr = String(result.year)+String(result.month);
                if(result.plans[day-1][currentMonthStr] != null && result.plans[day-1][currentMonthStr].size > 0){
                    // 일정 [size] 개
                    tdElements[j].textContent = '일정 '+result.plans[day-1][currentMonthStr].size+'개';
                }
            }
        }
        while(tmpDiv.firstChild){
            tbody.appendChild(tmpDiv.firstChild);
        }
        table.appendChild(tbody);
        document.getElementById("year").value = result.year;
        document.getElementById("month").value = result.month;
    }
}

function getThisMonth(year, month){
    return $.ajax({
        method: 'POST',
        url: '/getMonthlyPlan',
        beforeSend: function(xhr) {
            xhr.setRequestHeader("Accept", "application/json")
        },
        complete: function() {},
        error: function() {}
    }).then(function(planParam){
        displayMonthCalendar(planParam);
    });
}

// 달력 원소 클릭
function eventBinding() {
    document.addEventListener('DOMContentLoaded', function(){
        let tdEls = document.querySelectorAll('#calendar td');

        tdEls.forEach(tdEl => {
            tdEl.addEventListener('click',function(){
                // ajax
                fetch('/getDailyPlan',{
                    method: 'POST',
                    headears: {
                        'Content-Type': 'application/json'
                    },
                    data: JSON.stringify({data: this.textContent}),
                }).then(res => res.json()).then(data => {
                    // 응답 이후 뭘 하지
                }).catch(error => {console.error('Error:', error);})
            });
        });
    });

    $("#preYearBtn").click(function(){
        let param ={
            year: document.getElementById("year").value - 1,
            month: document.getElementById("month").value
        };
        return $.ajax({
            method: 'POST',
            url: '/getMonthlyPlan',
            data: JSON.stringify(param),
            contentType: 'application/json; charset=UTF-8'
        }).then(function(planParam){
            // return 된 날짜를 가지고 세팅
            displayMonthCalendar(planParam);
        });
    });

    $("#nextYearBtn").click(function(){
        let param ={
            year: document.getElementById("year").value + 1,
            month: document.getElementById("month").value
        };
        return $.ajax({
            method: 'POST',
            url: '/getMonthlyPlan',
            data: JSON.stringify(param),
            contentType: 'application/json; charset=UTF-8'
        }).then(function(planParam){
            // return 된 날짜를 가지고 세팅
            displayMonthCalendar(planParam);
        });
    });

    $("#preMonthBtn").click(function(){
        let param ={
            year: document.getElementById("year").value,
            month: document.getElementById("month").value - 1
        };
        return $.ajax({
            method: 'POST',
            url: '/getMonthlyPlan',
            data: JSON.stringify(param),
            contentType: 'application/json; charset=UTF-8'
        }).then(function(planParam){
            // return 된 날짜를 가지고 세팅
            displayMonthCalendar(planParam);
        });
    });

    $("#nextMonthBtn").click(function(){
        let param ={
            year: document.getElementById("year").value,
            month: document.getElementById("month").value + 1
        };
        return $.ajax({
            method: 'POST',
            url: '/getMonthlyPlan',
            data: JSON.stringify(param),
            contentType: 'application/json; charset=UTF-8'
        }).then(function(planParam){
            // return 된 날짜를 가지고 세팅
            displayMonthCalendar(planParam);
        });
    });

    $("#addPlanBtn").click(function (){
        return $.ajax({
            method: 'POST',
            url: '/addPage'
        });
    });

    $("#logoutBtn").click(function(){
        return $.ajax({
            method: 'POST',
            url: '/logOut'
        });
    });
}
function init(){
    //날짜 오늘 날짜로 설정
    var year = document.getElementById("year").value;
    var month = document.getElementById("month").value;
    getThisMonth(year,month);
    eventBinding();
}

$(document).ready(function(){
    init();
});