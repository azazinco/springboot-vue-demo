<template>
  <div>
    <div class='demo'>
      <video-player class="vjs-custom-skin"
                    ref="videoPlayer"
                    :options="playerOptions"
                    :playsinline="true"
      >
      </video-player>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";
import {videoPlayer} from 'vue-video-player/src'
import 'video.js/dist/video-js.css'
import 'vue-video-player/src/custom-theme.css'


export default {
  name: "VideoDetail",
  components: {
    videoPlayer,
  },
  data() {
    return {
      // videojs options
      playerOptions: {
        // aspectRatio: '16:9', // 将播放器置于流畅模式，并在计算播放器的动态大小时使用该值。值应该代表一个比例 - 用冒号分隔的两个数字（例如"16:9"或"4:3"）

        height: '360',
        autoplay: false,
        muted: true,
        language: 'en',
        playbackRates: [0.7, 1.0, 1.5, 2.0],
        sources: [{
          type: "video/mp4",
          // mp4
          src: "",
          // webm
          // src: "https://cdn.theguardian.tv/webM/2015/07/20/150716YesMen_synd_768k_vp8.webm"
        }],
        poster: "",
      }
    }
  },
  created() {
    let id = this.$route.query.id;
    request.get("/files/detail/" + id).then(res => {
      // console.log(res.data.url)
      this.playerOptions.sources[0].src = "http://localhost:9090/getVideo/video/" + res.data.flagName;
      console.log(res.data.flagName)
      console.log(this.playerOptions.sources[0].src);
    })
  },
  methods: {}
}
</script>

<style scoped>

</style>