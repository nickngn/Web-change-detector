




## HƯỚNG DẪN SỬ DỤNG CÔNG CỤ TỰ ĐỘNG KIỂM TRA THAY ĐỔI NỘI DUNG WEBSITE







### 1.	Mục đích
Để bảo đảm các phần thông tin quan trọng trên website không bị thay đổi, phá hoại gây thiệt hại cả về vật chất và uy tín cũng như sự thiếu hụt hoặc sai lệch thông tin đối với khách hàng.
Changed Content Detector (CCD) là công cụ giúp tự động kiểm tra nội dung trang web và gửi email thông báo nếu phát hiện sự thay đổi.

### 2.	Giới thiệu các thành phần trong phần mềm Changed Content Detector (CCD):
Thư mục chứa phần mềm gồm:
- log/: thư mục chứa lịch sử các lần kiểm tra của từng ngày.
- config/: thư mục chứa cấu hình của phần mềm
- differences/: chứa các file html đánh dấu sự khác nhau nhận được
- old_versions/: lưu trữ các bản trước của file sau khi phát hiện bị thay đổi 
- website_content/: lưu trữ nội dung của các file mới nhất dùng để so sánh mỗi lần gửi yêu cầu đến link.

Trong đó bên trong thư mục config chứa:
 
- File config.json cấu hình các link cần kiểm tra, tài khoản mail gửi đi thông báo, các tài khoản sẽ nhận được mail.
- File log-config.xml: là file cấu hình ghi log.
- File application.properties chứa cấu hình port, chu kỳ giám sát.

Chi tiết file config.json
 
- Các mục theo dõi gồm 1 title (tên đặt cho đường dẫn) và uri.
- sender: email của người gửi
- senderPassword: mật khẩu mail của người gửi
- receivers: các mail sẽ nhận thông báo khi phát hiện thay đổi.

Để thêm mục cần theo dõi:
- Trước khi chạy ứng dụng: chỉ cần thêm title (tùy ý, gợi nhớ và không trùng) và link như trên.
- Khi ứng dụng đang chạy : cũng thêm title cùng link nhưng phải gọi đến đường dẫn /reload-config để đường dẫn được đưa vào kiểm tra vào những lần sau.

Tương tự, khi không muốn kiểm tra đường dẫn nào nữa thì chỉ cần bỏ đường dẫn khỏi file config.json và gọi vào /reload-config.

Ứng dụng cũng cho phép cập nhật config mà không cần khởi động lại ứng dụng bằng cách truy cập : <ip>:<host>/reload-config với phương thức GET.

Ta có thể thay đổi trực tiếp các thông số tại __application.properties__, nhưng để ứng dụng sử dụng chúng thì cần khởi động lại bằng cách tắt mở trực tiếp hoặc gọi request: `<ip>:<host>/restart` ví dụ `localhost:8080/restart`

__Lưu ý__: 
- Nếu ứng dụng không được cấu hình SSL, mail của người gửi cần phải được cấp quyền truy cập, cách đơn giản nhất là “Cho phép truy cập kém an toàn” tại:
https://myaccount.google.com/lesssecureapps

### 3.	Các bước sử dụng:
Để chạy phần mềm, dùng terminal, cmd,… chỉ cần gọi lệnh : 
```
java -jar -Dfile.encoding=UTF8 changed-website-content-detector.jar
```
 
__Lưu ý__: `-Dfile.encoding=UTF8` là bắt buộc vì nội dung web kiểm tra gồm nhiều kí tự đặc biệt nên nếu thiếu sẽ gây lỗi.

Cập nhật thông số trên giao diện:
* Truy cập đường dẫn `<ip>:<host>/` trên trình duyệt và Chỉnh sửa cấu hình hoặc cập nhật các đường dẫn cần giám sát:

Cập nhật thông số không cần giao diện:
-	Để sửa port, thời gian lặp ta cập nhật trong file application.properties sau đó gọi lệnh <ip>:<host>/restart
-	Để cập nhật đường dẫn hoặc tài khoản email ta cập nhật trong file config.json sau đó gọi lệnh <ip>:<host>/reload-config
-	Ngoài ra, để kiểm tra ứng dụng đã chạy hay chưa ta có thể gọi <ip>:<host>/check-running
-	Để gửi thử email đảm bảo mọi người giám sát có thể nhận email, ta gọi <ip>:<host>/check-sending-email thì ứng dụng sẽ gửi 1 email test đến tất cả người nhận.


