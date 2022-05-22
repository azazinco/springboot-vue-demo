<template>
  <div>
    <div class='demo'>
      <video-player class="vjs-custom-skin"
                    ref="videoPlayer"
                    :options="playerOptions"
                    :playsinline="true"
                    @play="onPlayerPlay($event)"
                    @pause="onPlayerPause($event)"
                    @ended="onPlayerEnded($event)"
                    @loadeddata="onPlayerLoadeddata($event)"
                    @waiting="onPlayerWaiting($event)"
                    @playing="onPlayerPlaying($event)"
                    @timeupdate="onPlayerTimeupdate($event)"
                    @canplay="onPlayerCanplay($event)"
                    @canplaythrough="onPlayerCanplaythrough($event)"
                    @ready="playerReadied"
                    @statechanged="playerStateChanged($event)">
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
        autoplay: true,
        muted: true,
        language: 'en',
        playbackRates: [0.7, 1.0, 1.5, 2.0],
        sources: [{
          type: "video/mp4",
          // mp4
          src: "http://localhost:9090/files/alone/video/play/d16f22d0964e49bf87e1e1f0c57cb297.mp4",
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
      this.playerOptions.sources[0].src = res.data.url;
    })
  },
  methods: {}
}
</script>

<style scoped>

</style>