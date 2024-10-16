## Render

HotaruFX creates frames frame_00001.png, frame_00002.png, ...

To make a video from frames use [ffmpeg](https://ffmpeg.org/download.html):

```bash
ffmpeg -r 60 -i frame_%05d.png -pix_fmt yuv420p video.mp4
```
