/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/ClientSide/javascript.js to edit this template
 */

let click_button = document.getElementById("click_button");
click_button.addEventListener("click", function(){
    alert("Hi!");
    //event.target.style.background = "red";
    click_button.style.background = "red";
    
    
    const server_utc = Date.parse("2024-10-23T07:37:10.013394Z");
    var date = new Date(server_utc);// Milliseconds to date
    alert(date.toString());
})