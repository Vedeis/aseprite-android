# Aseprite Android Native Proto

Ini paket best-effort yang beneran bisa dibuild di GitHub Actions jadi APK debug.

Status:
- Native Android touchscreen app: YES
- Pixel canvas sederhana: YES
- Palette sederhana: YES
- Eraser: YES
- Aseprite stable source cloned by workflow: YES, v1.3.17.2
- Full Aseprite GUI: BELUM

Kenapa belum full Aseprite GUI?
Aseprite memakai laf, dan laf saat ini desktop-first untuk Windows/macOS/Linux.
Agar full Aseprite bisa jalan di Android, perlu backend laf Android:
- Android window/surface
- Skia Android surface
- touch -> mouse/gesture
- shortcut overlay
- file picker Android
- clipboard/storage Android

Cara build:
1. Upload isi folder ini ke repo GitHub.
2. Buka tab Actions.
3. Jalankan workflow "Build Aseprite Android Native Proto".
4. Download artifact.
5. Ambil APK dari folder dist/app-debug.apk.

Ini bukan 1.x-dev. Workflow clone Aseprite stable v1.3.17.2.
