<template>
    <div>
      在线人数：<input v-model="onlineCount" placeholder="waiting..."></input><br>
      在线用户：<input v-model="onlineList" placeholder="waiting..."></input><br>
      请输入用户名：<input v-model="userId"></input> <button @click="openSocket">建立连接</button>       
      <br>发送消息:
      <input v-model="to" placeholder="to...">
      <input v-model="sentText" placeholder="msg..."></input> <button @click="sendMsg">发送</button>
      <br>收到的消息:
        <input v-model="gotText" placeholder="waiting..."></input>


      <br>退出当前用户: <button @click="quit">退出</button>
    
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
        socket:'',
        onlineCount:0,
        onlineList:[],
        from:'',
        to:'',


      }
    },
    methods: {
        openSocket(){
            var iMessage={
              from:'',
              to:'',
              message:''
            }
            var socketUrl="ws://localhost:8083/api/pushMessage/"+this.userId
            this.socket = new WebSocket(socketUrl)
            this.socket.onopen=function open() {
                console.log('建立连接')
            }
            this.socket.onclose=function close() {
                console.log('关闭连接')
            }
           
            this.socket.onmessage = (msg)=> {
            //console.log(msg.data)
            var iMessage=JSON.parse(msg.data)

              console.log(iMessage);
            if("server"==iMessage.from){
              if("list"==iMessage.to){
                this.onlineList=iMessage.message.split(',')
              }else if("count"==iMessage.to){
                this.onlineCount=iMessage.message
              }
             
            }else if(iMessage.to==this.userId){
              this.gotText=iMessage.from+':'+iMessage.message  
            }
            
          }
        },
        sendMsg(){
          var iMessage={
              from:this.userId,
              to:this.to,
              message:this.sentText
            }
           console.log(JSON.stringify(iMessage));
            this.socket.send(JSON.stringify(iMessage))
        },
        quit(){
          this.socket.close()
        }
    },
   }
  </script>
  
