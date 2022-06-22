<template>
    <div>
      请输入用户名：<input v-model="userId"></input> <button @click="openSocket">建立连接</button>       
      <br>发送消息: <input v-model="sentText" placeholder="edit..."></input> <button @click="sendMsg">发送</button>
      <br>收到的消息: <input v-model="gotText" placeholder="waiting..."></input>
      <br>退出当前用户: <input v-model="userId" ></input><button @click="quit">退出</button>
    
    </div>
  </template>

  
  <script>
  export default {
    name: 'Websocket',
    components: {
      
    },
    data() {
      return {
        userId:'',
        sentText:'',
        gotText:'',
        socket:''
        
      }
    },
    methods: {
        openSocket(){
          var self = this
            var socketUrl="ws://localhost:8083/api/pushMessage/"+this.userId
            this.socket = new WebSocket(socketUrl);
            this.socket.onopen=function open() {
                console.log('建立连接')
            }
            this.socket.onclose=function close() {
                console.log('关闭连接')
            }
           
            this.socket.onmessage = (msg)=> {
            console.log(msg.data)
            this.gotText=msg.data
          }
        },
        sendMsg(){
            this.socket.send(this.userId+'说：我爱你,'+this.sentText)
        },
        quit(){
          this.socket.close()
        }
    },
   }
  </script>
  
