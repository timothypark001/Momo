function changePageTest() {
			
  var insert_value2 = `<div class="form-floating mb-3">
             <input type="text" name="membername" class="form-control" id="floatingInput" placeholder="인증번호를 입력하세요">
             <label for="floatingInput">인증번호</label>
             </div>
             <div class="d-grid gap-2">
               <button type="button" onclick="changePageTest()" class="btn btn-lg btn-secondary disabled">인증번호 입력</button>
             </div>`;
  
  var target_elem2 = document.getElementById('checkCode');
  target_elem2.innerHTML = insert_value2;
  
}

    
function sendNumber(){
   
  
  if($("#mail").val() != "" && $("#membername").val() != "") {
    
      $.ajax({
          url:"/mail/mailSendPw",
          type:"post",
          crossDomain: true,
          dataType:"json",
          data:{"mail" : $("#mail").val(),
      "membername" : $("#membername").val(),
      "memberid" : $("#memberid").val()
    
    
      },
      
      error:function(request, status, error){
        alert("메일 발송에 실패하였습니다");

        alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
          },
        
      success: function(data){
        alert("인증번호 발송");
        $("#Confirm").attr("value",data);
        $("#mail_number").css("display","block");	
      }
    });
      
     
    
  } else {
    alert("이메일과 이름을 입력해주세요");
  }
  
}

function confirmNumber(){
        var number1 = $("#number").val();
        var number2 = $("#Confirm").val();

        if(number1 == number2){
            alert("인증되었습니다.");
           $("#found_pw").css("display","block");
      
        }else{
            alert("번호가 다릅니다.");
        }
}
  
function resetPw() {
    var password1 = $("#password1").val();
    var password2 = $("#password2").val();

    if(password1 != "") {
      
      if(password1 == password2) {
        
        $.ajax({
          url:"/mail/resetPw",
          type:"post",
          crossDomain:true,
          data:{
            "memberid" : $("#memberid").val(),
            "password" : password1
          },
          
          error:function(request, status, error){
            alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
          },
          
          success : function() {
            alert("성공");
            window.location.href = "/";
          }
    
        });
  
      } else {
        alert("비밀번호를 다르게 입력하였습니다. 다시 입력해주세요");
      }
    
    } else {
      alert("비밀번호를 정확히 입력해주시기 바랍니다");
    }



}
