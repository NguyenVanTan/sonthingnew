!function($) {

	$(function() {
		if ($("#registerbtn").length > 0) {

			$('body')
					.on(
							'click',
							'#registerbtn',
							function() {
								var startTime = $("#startTime").val();
								var endTime = $("#endTime").val();

								var startTimeHour = startTime / 100;
								var startTimeMin = startTime
										- ((startTime / 100) * 100);
								var endTimeHour = endTime / 100;
								var endTimeMin = endTime
										- ((endTime / 100) * 100);
								var workTimeHour = endTimeHour - startTimeHour;
								var workTimeMin = endTimeMin - startTimeMin;
								if (workTimeMin < 0) {
									// 勤務時間(分)が0未満になる場合、勤務時間(時間)を1時間マイナスしたうえで、勤務時間(分)を60分プラスする
									workTimeHour--;
									workTimeMin += MIN_PER_ONE_HOUR;
								}

								var timeLag = workTimeHour * 60 + workTimeMin;

								var msg = "登録内容を更新します。宜しいですか？";
								if (timeLag >= 360 && timeLag < 480) {
									msg = "Warning：\n勤務時間が6時間以上、8時間未満の場合、定められた休憩時間は45分です。このまま登録して宜しいですか？";
								} else if (timeLag >= 480) {
									msg = "Warning：\n勤務時間が8時間以上の場合、定められた休憩時間は60分です。このまま登録して宜しいですか？";
								}
								if (!confirm(msg)) {
									return false;
								}
							});

		}

		if ($(".reject").length <= 0) {
			$(".reject_title").css("display", "none");

		}
		
		

		if ($("#hintdate").length > 0) {
			console.log($("#hintdate").text().replace("*", ""));
			$('.date_picker').datepicker({
				format : $("#hintdate").text().replace("*", "")

			}).on(
					'changeDate',
					function(ev) {
						if ($("#employmentDateId").val() == "") {
							$("#payVacationLeftJoinDateId").val("");
						} else {
							$("#payVacationLeftJoinDateId").val(
									$("#employmentDateId").val());
						}
					});

			$("#payVacLeftJoinId").on(
					"blur",
					function(ev) {
						if ($("#payVacLeftJoinId").val() == "") {
							$("#payVacationLeftJoinDateId").val("");
						} else {
							$("#payVacationLeftJoinDateId").val(
									$("#employmentDateId").val());
						}
					});

			$("#payVacLeftJoinTempId").on("blur", function(ev) {
				if ($("#payVacLeftJoinTempId").val() == "") {
					$("#payVacationLeftJoinTempDateId").val("");
				}
			});

			$("#payVacLeftLastYearId").on("blur", function(ev) {
				if ($("#payVacLeftLastYearId").val() == "") {
					$("#payVacationLeftLastYearDateId").val("");
				}
			});

			$("#payVacLeftThisYearId").on("blur", function(ev) {
				if ($("#payVacLeftThisYearId").val() == "") {
					$("#payVacationLeftThisYearDateId").val("");
				}
			});

		} else {
			$('.date_picker').datepicker({
				format : "yyyy/mm/dd"
			});
		}

		$('#avatar').hover(function() {
			$('#changeImagediv').show();
		}, function() {
			$('#changeImagediv').hide();
		});

		$('#changeImagediv').click(function(event) {
			event.preventDefault();
			$('#uploadField').trigger("click");
		});

		updateFixedColumnTable();
		peerRaterTable(true);
	});

}(window.jQuery);

var peerRaterTbl;

function peerRaterTable(stateSave){

	if($('#peerRaterTable').length <=0){
		return false;
	}

	$.fn.dataTable.ext.order['dom-checkbox'] = function  ( settings, col )
	{
		return this.api().column( col, {order:'index'} ).nodes().map( function ( td, i ) {
			return $('input', td).prop('checked') ? '1' : '0';
		} );
	}

	peerRaterTbl = $('#peerRaterTable').DataTable({
		paging : false,
		searching: false,
		autoWidth : true,
		bFilter : false,
        destroy: true,
		info : false,
		"aaSorting": []
		,
		"aoColumnDefs" : [
			{ "bSortable": false, "aTargets": [ 2,3,4 ] },
			{ "orderSequence": [ "desc", "asc"], "targets": [ 0,1 ] },
		],
		"columns": [
			{ "orderDataType": "dom-checkbox" },
			{ "orderDataType": "dom-checkbox" },
			null,
			null,
			null
		],
		stateSave: stateSave
	});
}

$(window).bind('beforeunload', function() {

	if($('#peerRaterTable').length > 0){
		peerRaterTbl.state.clear();
	}
});

function updateFixedColumnTable()
{
	if ($('#tblWrapper').length > 0) {
		var table = $('#timeSheetTable').DataTable({
			scrollY : "500px",
			scrollX : true,
			scrollCollapse : true,
			paging : false,
			ordering : false,
			autoWidth : true,
			bSort : false,
			bFilter : false,
			info : false,
			columnDefs : [ {
				width : 70,
				targets : 0
			}, {
				width : 60,
				targets : 1
			}, {
				width : 60,
				targets : 2
			}, {
				width : 100,
				targets : 3
			}, {
				width : 100,
				targets : 4
			}, {
				width : 90,
				targets : 5
			}, {
				width : 90,
				targets : 6
			}, {
				width : 90,
				targets : 7
			}, {
				width : 90,
				targets : 8
			}, {
				width : 90,
				targets : 9
			}, {
				width : 80,
				targets : 10
			}, {
				width : 115,
				targets : 11
			}, {
				width : 115,
				targets : 12
			}, {
				width : 115,
				targets : 13
			}, {
				width : 70,
				targets : 14
			}, {
				width : 90,
				targets : 15
			}, {
				width : 115,
				targets : 16
			}, {
				width : 116,
				targets : 17
			}, {
				width : 100,
				targets : 18
			}, {
				width : 80,
				targets : 19
			}, {
				width : 70,
				targets : 20
			}, {
				width : 70,
				targets : 21
			}, {
				width : 80,
				targets : 22
			}, {
				width : 80,
				targets : 23
			}, {
				width : 90,
				targets : 24
			}, {
				width : 80,
				targets : 25
			}, {
				width : 80,
				targets : 26
			}, {
				width : 70,
				targets : 27
			}, {
				width : 90,
				targets : 28
			}, ]
		});

		new $.fn.dataTable.FixedColumns(table, {
			leftColumns : 3
		});

	}
}