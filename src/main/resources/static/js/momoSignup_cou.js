'use strict';

const form = document.querySelector('#form_wrap');
const checkAll = document.querySelector('#terms_check_all input');
const checkBoxes = document.querySelectorAll('#form_check input');
const submitButton = document.querySelector('#nextStep');

const agreements = {
  termsOfService: false,
  privacyPolicy: false,
};

form.addEventListener('button', (e) => e.preventDefault()); // 새로고침(submit) 되는 것 막음

checkBoxes.forEach((item) => item.addEventListener('input', toggleCheckbox));

function toggleCheckbox(e) {
  const { checked, id } = e.target;  
  agreements[id] = checked;
  this.parentNode.classList.toggle('active');
  checkAllStatus();
  toggleSubmitButton();
}

function checkAllStatus() {
  const { termsOfService, privacyPolicy } = agreements;
  if (termsOfService && privacyPolicy) {
    checkAll.checked = true;
  } else {
    checkAll.checked = false;
  }
}

function toggleSubmitButton() {
  const { termsOfService, privacyPolicy } = agreements;
  if (termsOfService && privacyPolicy) {
    submitButton.disabled = false;
  } else {
    submitButton.disabled = true;
  }
}

checkAll.addEventListener('click', (e) => {
  const { checked } = e.target;
  if (checked) {
    checkBoxes.forEach((item) => {
      item.checked = true;
      agreements[item.id] = true;
      item.parentNode.classList.add('active');
    });
  } else {
    checkBoxes.forEach((item) => {
      item.checked = false;
      agreements[item.id] = false;
      item.parentNode.classList.remove('active');
    });
  }
  toggleSubmitButton();
});

const open = document.querySelectorAll("#open");
const modalBox = document.querySelectorAll("#modal-box");
const close = document.querySelectorAll("#close");

open[0].addEventListener("click", () => {
  modalBox[0].classList.toggle("active"); 
});

close[0].addEventListener("click", () => {
  modalBox[0].classList.remove("active"); 
});

open[1].addEventListener("click", () => {
  modalBox[1].classList.toggle("active"); 
});

close[1].addEventListener("click", () => {
  modalBox[1].classList.remove("active"); 
});