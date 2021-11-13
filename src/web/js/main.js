// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.

 
/* eslint-disable no-new */
var vue = new Vue({
  el: '#app',
  data(){
      return{
        text:"WELCOME TO HDFS WEB!",
        FileList: null,
        dirName: null,
        file: '',
        mkdirDone: false,
        nowPathList: ["/"],
        nowPath : ''          
      }
  },
  mounted () {
    axios.get('http://localhost:8080/listFile?folder=/')
         .then(response => (this.FileList = response.data))
  },
  methods: {
    mkdir: function(){
      var nowPath = this.nowPath==''?'/':this.nowPath
      var absolutePath = nowPath + this.dirName
      var that = this
      axios.post('http://localhost:8080/mkdir',"absolutePath=" + absolutePath )
      .then(function(res){
          that.dirName = null
          toastr.success("创建文件夹成功")
          axios.get('http://localhost:8080/listFile?folder='+nowPath)
         .then(response => (that.FileList = response.data))
      })
    },
    remove: function(e){
      var nowPath = this.nowPath==''?'/':this.nowPath
      var path = this.nowPath + '/' + e.srcElement.dataset.name
      var that = this
      axios.post('http://localhost:8080/removeFile',"path=" + path )
      .then(function(res){
          toastr.success("删除文件/文件夹成功")
          axios.get('http://localhost:8080/listFile?folder='+nowPath)
         .then(response => (that.FileList = response.data))
      })
    },
    download: function(e){
      var nowPath = this.nowPath==''?'/':this.nowPath
      var path = nowPath + e.srcElement.dataset.name
      var dst = '/home/ubuntu/Code/downloadTest/' + e.srcElement.dataset.name
      axios.post('http://localhost:8080/downloadFile',"srcString=" + path + "&dstString=" + dst)
      .then(function(res){
        toastr.success("文件/文件夹下载成功"+"\n"+"/home/ubuntu/Code/downloadTest/")
      })
    },
    intoDir: function(e){
      this.nowPathList.push(e.srcElement.dataset.name)
      var nowPath = '/'
      for(var i=1;i<this.nowPathList.length;i++){
        nowPath = nowPath + this.nowPathList[i] + '/'
      }
      this.nowPath = nowPath
      axios.get('http://localhost:8080/listFile?folder='+nowPath)
         .then(response => (this.FileList = response.data))
    },
    backDir: function(e){
      this.nowPathList.splice(-1,1)
      var nowPath = '/'
      for(var i=1;i<this.nowPathList.length;i++){
        nowPath = nowPath + this.nowPathList[i] + '/'
      }
      axios.get('http://localhost:8080/listFile?folder='+nowPath)
         .then(response => (this.FileList = response.data))
      if(nowPath=='/'){
        this.nowPath = ''
      }else{
        this.nowPath = nowPath
      }
      
    },
    getFile(event) {
      this.file = event.target.files[0]
      console.log(this.file)
    },
    submitForm(event) {
      event.preventDefault();
      var nowPath = this.nowPath==''?'/':this.nowPath
      let formData = new FormData();
      formData.append('file', this.file);
      formData.append('dstString',this.nowPath)

      let config = {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }
      alert(nowPath)
      axios.post('http://localhost:8080/uploadFile', formData, config)
      .then(function (res) {
        toastr.success("上传文件成功,请刷新查看")
        axios.get('http://localhost:8080/listFile?folder='+nowPath)
         .then(response => (this.FileList = response.data))
      })
      

      
    }

  }
})

