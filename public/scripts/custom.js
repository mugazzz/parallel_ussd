$(document).ready(function() {   
   
    let testCases = {};
    testCases['SelectScenario'] = ['Select Test Case..']
    testCases['OPT IN'] = ['','Long Code'];
    testCases['OPT OUT'] = ['','Long Code'];
    testCases['RECHARGE'] = ['','Recharge_More_Time','Recharge_More_International','Recharge_More_Data','Recharge_More_Credit'];
    testCases['LIVE USAGE DATA'] = ['','DATA_REGULAR','DATA_SOCIAL'];
    testCases['LIVE USAGE VOICE'] = ['','Du-du calls: VOICE_DU','Du-du calls: VOICE_DU_OFF','Du-du calls: VOICE_DU_WK','Du-du calls: VOICE_DU_OFF_WK','Du-CUG calls: VOICE_DU_CUG','Du-CUG calls: VOICE_DU_OFF_CUG','Du-CUG calls: VOICE_DU_WK_CUG','Du-CUG calls: VOICE_DU_OFF_WK_CUG','Roaming calls: VOICE_INT_ROAM','Du-National calls: VOICE_NAT','Du-National calls: VOICE_NAT_OFF','Du-National calls: VOICE_NAT_WK','Du-National calls: VOICE_NAT_OFF_WK','Du-International calls: VOICE_INT_IDD','Du-International calls: VOICE_INT_IDD_OFF','Du-International calls: VOICE_INT_IDD_WK','Du-International calls: VOICE_INT_IDD_OFF_WK'];
    testCases['LIVE USAGE VIDEO CALL'] = ['','Du-du calls: VIDEO_DU','Du-du calls: VIDEO_DU_OFF','Du-du calls: VIDEO_DU_WK','Du-du calls: VIDEO_DU_OFF_WK','Du-CUG calls: VIDEO_DU_CUG','Du-CUG calls: VIDEO_DU_OFF_CUG','Du-CUG calls: VIDEO_DU_WK_CUG','Du-CUG calls: VIDEO_DU_OFF_WK_CUG','Roaming calls: VIDEO_INT_ROAM','Du-National calls: VIDEO_NAT','Du-National calls: VIDEO_NAT_OFF','Du-National calls: VIDEO_NAT_WK','Du-National calls: VIDEO_NAT_OFF_WK','Du-International calls: VIDEO_INT_IDD','Du-International calls: VIDEO_INT_IDD_OFF','Du-International calls: VIDEO_INT_IDD_WK','Du-International calls: VIDEO_INT_IDD_OFF_WK'];
	testCases['LIVE USAGE SMS'] = ['','SMS_DU_TO_DU','SMS_DU_TO_Local','SMS_DU_International'];

    let paramList = {};
    paramList['OPT IN'] = ['MSISDN','Product_Name'];
    paramList['OPT OUT'] = ['MSISDN','Product_Name'];
    paramList['RECHARGE'] = ['MSISDN','Recharge_Coupon'];
    paramList['LIVE USAGE DATA'] = ['MSISDN'];
    paramList['LIVE USAGE VOICE'] = ['MSISDN','Call_TO_MSISDN','CALL_DURATION'];
	paramList['LIVE USAGE VIDEO CALL'] = ['MSISDN','Call_TO_MSISDN','CALL_DURATION'];
    paramList['LIVE USAGE SMS'] = ['MSISDN','RECEIVER_MSISDN','Message_To_Send','SMS_COUNT'];
    paramList['P2P TRANSFER'] = ['MSISDN','TRANSFER_TO_MSISDN','TRANSFER_AMOUNT'];
    paramList['BALANCE ENQUIRES'] = ['MSISDN'];

    
    
    function createParamList() {
        //console.log("testCase change");
       // $("#param-list-container").empty();
        $(this).closest('tr').find(".param-list").empty();
        //$(".td-param").remove();
        $(this).closest('tr').find(".td-param").remove();
        //let selectedTestScenario = $("#testScenario option:selected").val();
        //let selectedTestCase = $("#testCase option:selected").val();
        let selectedTestScenario = $(this).closest('tr').find("[name='testScenario'] option:selected").val();
        let selectedTestCase = $(this).closest('tr').find("[name='testCase'] option:selected").val();
        
        console.log("selectedTestScenario:"+selectedTestScenario+", selectedTestCase:"+selectedTestCase);
    
        if(selectedTestCase != ""){ 

            let sParam = paramList[selectedTestScenario];
            let ctrlHtml ='';
            $.each(sParam,function(val,text){
                if(text==="Product_Name"){
                    ctrlHtml +=`<td class="td-param"><select id="${text}" name="${text}" class="form-control custom-select-sm custom-select cust-form-ctrl" placeholder="${text}"></select></td>`;
                }
                else{
                ctrlHtml += `<td class="td-param"><input type="text" class="form-control form-control-sm cust-form-ctrl" name="${text}" placeholder="${text}"></td>`;
                }
            });

         /*   let copyBtn = `<td class="td-param"><a href="#" title="copy row" class="btn btn-light  copy-ts-btn"><i class="far fa-copy"></i></a></td>`;
            let addRowBtn = `<td class="td-param"><a href="#" title="add row" class="btn btn-light addrow-ts-btn"><i class="fas fa-plus"></i></a></td>`;
            let delRowBtn = `<td class="td-param"><a href="#" title="delete row" class="btn btn-light delrow-ts-btn"><i class="far fa-trash-alt"></i></a></td>`;*/

            let sHtml =`<td class="param-list" id="param-list-container">${ctrlHtml}</td>`;
            //console.log(sDiv);
            //$("#param-list-container").replaceWith(sHtml);
            $(this).closest('tr').find(".param-list").replaceWith(sHtml);
            
           // bindEvtToBtns(); //bind events for all buttons
           var sProd = $(this).closest('tr').find("[name='Product_Name']");
            
            if(sProd.length){
                $(sProd).append("<option></option>");
                $.each(window.productsData.products, function( index, value ) {
                    $(sProd).append("<option value='"+value+"'>"+value+"</option>");
                });

                $(sProd).select2({
                    placeholder: 'Select Product..'
                });
            }
        }//end if
    } //end function

    /** Test Cases Data table customizations */
    var table = $('#test-case-list').DataTable({
        scrollX:        true,
        scrollCollapse: true,
        autoWidth:    true,  
        paging:      true,  
        columnDefs: [ {
            orderable: false,
            className: 'select-checkbox',
            targets:   0
        } ],
        select: {
            style:    'multi',
            selector: 'td:first-child'
        }
    }); 
    
/* reports page table */
$('#report-list').DataTable({"order":[[ 0, 'desc' ]]});

  /*  $('#test-case-list tbody').on( 'click', 'tr', function () {
        $(this).toggleClass('selected');
    } );*/

    /** Buttons delete and execute functionality */

    $('#btn-delete-row').click( function () {
        if(table.rows( { selected: true } ).count() > 0){
            let RowIdList = [];
            let TestCaseIdList = [];
            let dataArr = table.rows( { selected: true } ).data();
            let jdataArr = table.rows( { selected: true } ).nodes().to$();
            console.log(table.rows( { selected: true } ).nodes().to$());
            $.each(dataArr,function(index,value){
                //RowIdList.push(value[0]);
                TestCaseIdList.push(value[1]);
            });

            $.each(jdataArr,function(index,value){
                RowIdList.push(value.children[0].dataset.testcase_id);
            });
            
             
            $("#delete_test_id").val(TestCaseIdList.join(","));
            $("#delete_test_row_id").val(RowIdList.join(","));
            $('#DeleteModal').modal('show');
            //table.row('.selected').remove().draw( false );
        }
        
    } );

    $('#btn-execute').click( function () {
        if(table.rows( { selected: true } ).count() > 0){
            let RowIdList = [];
            let TestCaseIdList = [];
            let dataArr = table.rows( { selected: true } ).data();
            let jdataArr = table.rows( { selected: true } ).nodes().to$();
            console.log(table.rows( { selected: true } ).nodes().to$());
            $.each(dataArr,function(index,value){
                TestCaseIdList.push(value[1]);
            });
            $.each(jdataArr,function(index,value){
                RowIdList.push(value.children[0].dataset.testcase_id);
            });
            $("#execute_test_id").val(TestCaseIdList.join(","));
            $("#execute_test_row_id").val(RowIdList.join(","));
            $('#ExecuteModal').modal('show');
            //table.row('.selected').remove().draw( false );
        }
        
    } );

    $('#execute-row-form').submit(function(evt){

        evt.preventDefault();
        //$('#ExecuteModal').modal('hide');
        var $form = $( this ),
        testId = $form.find( "#execute_test_id" ).val(),
        testRowId = $form.find("#execute_test_row_id").val(),
        url = $form.attr( "action" );
        
        //var sHtml = `<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
         //           Executing...`;
        //$form.find( ".btn-modal-execute" ).prop("disabled",true).html(sHtml);
        $('#ExecuteModal').modal('hide');
        $('#myLoadingModal').modal('show');
        $.post(url, {execute_test_id:testId,execute_test_row_id:testRowId},function(data, status){
            //alert("Data: " + data + "\nStatus: " + status);
            if(status =="success"){
                $form.find( ".btn-modal-execute" ).prop("disabled",false);
                //alert(data.code);
                setTimeout(function(){$('#myLoadingModal').modal('hide');location.reload();},1000);
                
            }
          });
          
        //$('#execute-row-form').submit();
    })
    

    /** navigation bar customization **/
    $('.nav-item a').on('click', function (e) {
        $('.nav-item a').removeClass("active");
        $(this).addClass("active");
      });


    $(".addrow-ts-btn").on("click", function (evt){
        evt.preventDefault();
        let sHtml = ` <tr class="tr-clone">  
                        <td>
                            <div class="custom-control custom-checkbox">
                            <input type="checkbox" class="form-check-input" id="customCheck1" checked>
                            </div>
                        </td>
                        <td class="td-row-btn"><a href="#" title="copy row" class="btn btn-light  copy-ts-btn"><i class="far fa-copy"></i></a></td> 
                        <td class="td-row-btn"><a href="#" title="delete row" class="btn btn-light delrow-ts-btn"><i class="far fa-trash-alt"></i></a></td>
                        <td>
                            <input type="text" name="testCaseNo" class="form-control form-control-sm cust-form-ctrl"  placeholder="Test Case ID"> 
                        </td>
                        <td>
                            <select name="testScenario" class="form-control custom-select-sm custom-select cust-form-ctrl" placeholder="Test Scenario">
                                    <option value="SelectScenario">Select Scenario..</option>
                                    <option value="OPT IN">OPT IN</option>
                                    <option value="OPT OUT">OPT OUT</option>
                                    <option value="RECHARGE">RECHARGE</option>
                                    <option value="LIVE USAGE DATA">LIVE USAGE DATA</option>
                                    <option value="LIVE USAGE VOICE">LIVE USAGE VOICE</option>
									<option value="LIVE USAGE VOICE">LIVE USAGE VIDEO CALL</option>
                                    <option value="LIVE USAGE SMS">LIVE USAGE SMS</option>
                                    <option value="P2P TRANSFER">P2P TRANSFER</option>
                                    <option value="BALANCE ENQUIRES">BALANCE ENQUIRES</option>
                            </select>
                        </td>
                        <td>
                            <select name="testCase" class="form-control custom-select-sm custom-select cust-form-ctrl" placeholder="Test Case">
                                <option value="SelectTestCase">Select Test Case..</option>
                            </select>
                        </td>
                        <td>
                            <select name="testDevice" class="form-control custom-select-sm custom-select cust-form-ctrl" placeholder="Test Device">
                                <option value="SelectDevice">Select Device..</option>
                                <option value="Device1">Device1</option>
                                <option value="Device2">Device2</option>
                            </select>
                        </td>
                        <td class="param-list" id="param-list-container"></td>
                    </tr>`
        
        $(sHtml).insertAfter(".tr-clone:last");
        bindEvntToCtrls();
    });

    $(".save-ts-btn").on("click", function (evt){
        evt.preventDefault();

        $(".tr-clone input:checked").each(function(){

            var inputObj = {
                testCaseNo: $(this).closest('tr').find("[name=testCaseNo]").val(),
                testScenario:$(this).closest('tr').find("[name=testScenario]").val(),
                testCase:$(this).closest('tr').find("[name=testCase]").val(),
                testDevice:$(this).closest('tr').find("[name=testDevice]").val(),
                MSISDN:$(this).closest('tr').find("[name='MSISDN']").val(),
                Product_Name:$(this).closest('tr').find("[name='Product_Name']").val(),
                Recharge_Coupon:$(this).closest('tr').find("[name='Recharge_Coupon']").val(),
                Call_TO_MSISDN:$(this).closest('tr').find("[name='Call_TO_MSISDN']").val(),
                CALL_DURATION:$(this).closest('tr').find("[name='CALL_DURATION']").val(),
                RECEIVER_MSISDN:$(this).closest('tr').find("[name='RECEIVER_MSISDN']").val(),
                Message_To_Send:$(this).closest('tr').find("[name='Message_To_Send']").val(),
                SMS_COUNT:$(this).closest('tr').find("[name='SMS_COUNT']").val(),
                TRANSFER_AMOUNT:$(this).closest('tr').find("[name='TRANSFER_TO_MSISDN']").val(),
                TRANSFER_TO_MSISDN:$(this).closest('tr').find("[name='TRANSFER_AMOUNT']").val()
            }
            console.log(inputObj);
            $.ajax({
                type: "POST",
                url: "/save",
                data: $.param(inputObj),
                success: function(data, status, xhr) {
                    console.log("form save called",data,status,xhr);
                    $('.custom-alert').show();
                    setTimeout(function(){ $('.custom-alert').hide();},2000);
                },
                error: function(){
                    console.log("error ajax submission");
                }
            });
        });
        setTimeout(function(){ location.reload();},500);

    });

    bindEvntToCtrls();//call bind events for all controls on page load

    function bindEvntToCtrls(){

        $(".copy-ts-btn").on("click", function (evt){
            evt.preventDefault();
            evt.stopImmediatePropagation();
            let objRow = $(this).closest(".tr-clone");
            let objNewRow = $(objRow).clone(true,true).insertAfter(".tr-clone:last");
            $(objNewRow).find("[name=testScenario]").val($(objRow).find("[name=testScenario]").val());
            $(objNewRow).find("[name=testCase]").val($(objRow).find("[name=testCase]").val()).trigger("change");
            $(objNewRow).find("[name=testDevice]").val($(objRow).find("[name=testDevice]").val());
            $(objNewRow).find("[name=Product_Name]").val($(objRow).find("[name=Product_Name]").val());
        });
    
        $(".delrow-ts-btn").on("click", function (evt){
            evt.preventDefault();
            console.log("row deleted");
            $(this).closest(".tr-clone").remove();
        }); 
        
        $("tr [name=testScenario]").on("change",function(){
            $(this).closest('tr').find(".param-list").empty();
            $(this).closest('tr').find("[name='testCase']").show();
            //$(this).closest(".param-list").empty();
            //let testCaseList = $("#testCase");
            let testCaseList =  $(this).closest('tr').find("[name='testCase']");
            let sTestScenario = $(this).val();
            //$("#testCase option").remove();
            $(testCaseList).children("option").remove();
            let testCase = testCases[sTestScenario];
            let options = [];
            if(testCaseList.prop) { options = testCaseList.prop('options');}
            else { options = testCaseList.attr('options');}
    
            $.each(testCase,function(val,text){
                options[options.length] = new Option(text,text);
            });
    
            if(sTestScenario =="P2P TRANSFER" || sTestScenario =="BALANCE ENQUIRES")
            {
                createParamList.call(this);
                $(this).closest('tr').find("[name='testCase']").hide();
            }
        });
    
        $("tr [name=testCase]").on("change", createParamList);
    
    }
    
});  