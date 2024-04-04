function getDailyPlan(){
    return $.ajax({
        url: '/getDailyPlan',
        type: 'GET',
        dataType: 'json',
        success: function(response){
            var table = $('#dailyPlanTable tbody');
            table.empty();

            document.getElementById('year').value = response.year;
            document.getElementById('month').value = response.month;

            $.each(response, function(index, plan){
                var row =  $('<tr id=' + plan.id + '></tr>');
                row.append($('<td></td>').text(index+1));
                row.append($('<td></td>').text(plan.title));
                row.append($('<td></td>').text(plan.startDtStr));
                row.append($('<td></td>').text(plan.endDtStr));
            });
        },
        error: function(xhr, status, error) {
            console.error("Error: " + status + " " + error);
        }
    });
}

function eventBinding(){
    document.addEventListener('DOMContentLoaded',function(){
        let tdEls = document.querySelectorAll('#dailyPlanTable td');

        tdEls.forEach(tdEl => {
            tdEl.addEventListener('click', function(){
                //ajax
                fetch('/getPlan',{
                    method: 'POST',
                    headears: {
                        'Content-Type': 'application/json'
                    },
                    data: JSON.stringify({data: this.id}),
                }).then(res => res.json()).then(data => {
                    // 응답 이후 뭘 하지
                }).catch(error => {console.error('Error:', error);})
            });
        });
    });

    $("#monthlyPlanBtn").click(function(){
        let param = {
            year: document.getElementById('year').value,
            month: document.getElementById('month').value
        };
        return $.ajax({
            method: 'POST',
            url: '/getMonthlyPlan',
            data: JSON.stringify(param),
            contentType: 'application/json; charset=UTF-8'
        });
    });

    $('#addPlanBtn').click(function(){
        return $.ajax({
            method: 'POST',
            url: '/addPage'
        });
    })
}
function init(){
    getDailyPlan();
    eventBinding();
}

$(document).ready(function(){
    init();
});