/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/ClientSide/javascript.js to edit this template
 */

let click_button = document.getElementById("click_button");
click_button.addEventListener("click", function(){
    
    
    
    //alert("Hi!");
    //event.target.style.background = "red";
    //click_button.style.background = "red";
    
    getDate();
    
    
    
})

async function getDate() {
  const url = "server_time";
  try {
    const response = await fetch(url);
    if (!response.ok) {
      throw new Error(`Response status: ${response.status}`);
    }

    const utc_server_time = await response.text();
    const server_utc = Date.parse(utc_server_time);
    const local_datetime = new Date(server_utc);// Milliseconds to date
    alert(local_datetime.toString());
    
  } catch (error) {
    console.error(error.message);
  }
}
