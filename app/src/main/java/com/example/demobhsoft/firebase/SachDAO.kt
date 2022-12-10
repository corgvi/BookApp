package com.example.demobhsoft.firebase

import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.demobhsoft.model.SachModel
import com.example.demobhsoft.model.UserModel
import com.example.demobhsoft.screen.LoginActivity
import com.example.demobhsoft.utils.Constant
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log

class SachDAO {

    private val db = Firebase.firestore
    private val TAG = "SachDAO"

    fun getListSach(): ArrayList<SachModel> {
        var listSach = ArrayList<SachModel>()
        db.collection(Constant.SACH.TB_SACH)
            .get()
            .addOnSuccessListener { task ->
                listSach.clear()
                for (document in task) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val sach: SachModel = document.toObject(SachModel::class.java)
                    listSach.add(sach)
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
        Log.d(TAG, "getListUser: listUser: ${listSach.size}")
        return listSach
    }

    fun initSachModel(){
        var list = ArrayList<SachModel>()
        list.add(
            SachModel(
                "Sach_1", "Những Nguyên Tắc Vàng Của Napoleon Hill",
            "Napoleon Hill (1883 – 1970) được mệnh danh là “Người đặt nền tảng cho môn khoa học Thành công”. Ông chính là người khơi nguồn cho dòng sách khơi dậy những tiềm năng của con người với hàng loạt những tác phẩm nổi tiếng như Law of Success (Quy luật của sự thành công), Think and Grow Rich (Cách nghĩ để thành công), The Magic Ladder of Success (Những nấc thang huyền bí dẫn tới thành công), Success through A Positive Mental Attitude (Thành công từ cách tư duy tích cực)… Những tư tưởng thể hiện trong các tác phẩm của ông đều hướng đến mục tiêu chung là nâng cao tầm phát triển của con người.",
            78_000, "https://cdn0.fahasa.com/media/catalog/product/8/9/8935086847817_1.jpg",
            4.5F,
            "Napoleon Hill", "NXB Tổng Hợp TPHCM")
        )
        list.add(
            SachModel(
                "Sach_2", "Thoát Khỏi Những Nỗi Sợ Hãi Của Bạn - Để Tiến Bước Tới Thành Công",
            "Đừng để nỗi sợ hãi níu chân bạn tận hưởng thành công trong sự nghiệp và cuộc sống. Đã tới lúc bạn cần làm chủ nỗi sợ hãi và tăng cường sức bật tinh thần của mình để có thể kiểm soát cuộc sống và đạt được tầm nhìn về thành công của mình.Nỗi sợ hãi là một trong những trở ngại lớn nhất đối với thành công, hạnh phúc và cảm giác thỏa nguyện, và đó cũng là một trong những trở ngại khó khắc phục nhất. Nỗi sợ hãi bắt nguồn từ sâu trong tiềm thức và khiến những suy nghĩ chiếm hữu tâm trí bạn trở nên u tối, nhận thức và hành động của bạn do đó cũng trở nên lạc lối. ",
            65_000, "https://cdn0.fahasa.com/media/flashmagazine/images/page_images/thoat_khoi_nhung_noi_so_hai_cua_ban___de_tien_buoc_toi_thanh_cong/2022_07_20_14_48_50_1-390x510.jpg",
            4.5F,
            "Napoleon Hill", "Dân Trí")
        )
        list.add(
            SachModel(
                "Sach_10", "Công Thức Tự Tin - Để Vươn Tới Sự Tự Lập Và Thành Công",
            "Thiếu tự tin là một trong những căn bệnh phổ biến nhất của thế giới ngày nay, gây ra sự bất lực tràn lan, thiếu tự chủ, thiếu mục đích, trì trệ và tuyệt vọng đặc trưng của xã hội hiện đại. Nhưng như ông trùm ngành thép Andrew Carnegie đã nhấn mạnh với Napoleon Hill: “Tự tin là một trạng thái tâm trí, là yếu tố cần thiết để thành công, và điểm khởi đầu của việc phát triển lòng tự tin và sự rõ ràng về mục đích.”",
            78_000, "https://cdn0.fahasa.com/media/flashmagazine/images/page_images/cong_thuc_tu_tin___de_vuon_toi_su_tu_lap_va_thanh_cong/2022_06_20_14_48_00_1-390x510.jpg",
            4.2F,
            "Napoleon Hill", "NXB Dân Trí")
        )
        list.add(
            SachModel(
                "Sach_4", "Tư Duy Làm Giàu - Những Bài Nói Chuyện Bất Hủ Của Napoleon Hill",
            "Napoleon Hill (1883 – 1970) được mệnh danh là “Người đặt nền tảng cho môn khoa học Thành công”. Ông chính là người khơi nguồn cho dòng sách khơi dậy những tiềm năng của con người với hàng loạt những tác phẩm nổi tiếng như Law of Success (Quy luật của sự thành công), Think and Grow Rich (Cách nghĩ để thành công), The Magic Ladder of Success (Những nấc thang huyền bí dẫn tới thành công), Success through A Positive Mental Attitude (Thành công từ cách tư duy tích cực)… Những tư tưởng thể hiện trong các tác phẩm của ông đều hướng đến mục tiêu chung là nâng cao tầm phát triển của con người.",
            100_000, "https://cdn0.fahasa.com/media/catalog/product/8/9/8935086847817_1.jpg",
            3.5F,
            "Napoleon Hill", "NXB Tổng Hợp TPHCM")
        )
        list.add(
            SachModel(
                "Sach_5", "Chiến Thắng Con Quỷ Trong Bạn",
            "Napoleon Hill (1883 – 1970) được mệnh danh là “Người đặt nền tảng cho môn khoa học Thành công”. Ông chính là người khơi nguồn cho dòng sách khơi dậy những tiềm năng của con người với hàng loạt những tác phẩm nổi tiếng như Law of Success (Quy luật của sự thành công), Think and Grow Rich (Cách nghĩ để thành công), The Magic Ladder of Success (Những nấc thang huyền bí dẫn tới thành công), Success through A Positive Mental Attitude (Thành công từ cách tư duy tích cực)… Những tư tưởng thể hiện trong các tác phẩm của ông đều hướng đến mục tiêu chung là nâng cao tầm phát triển của con người.",
            78_000, "https://cdn0.fahasa.com/media/catalog/product/c/h/chien_thang_con_quy_trong_ban_10.2021_bia_01.jpg",
            4.5F,
            "Napoleon Hill", "NXB Lao Động")
        )
        list.add(
            SachModel(
                "Sach_6", "Những Nguyên Tắc Vàng Của Napoleon Hill",
            "Cuốn sách là cuộc trò chuyện của Napoleon Hill và Con Quỷ. Sau bao nhiêu năm miệt mài nghiên cứu cuối cùng ông cũng phát hiện ra Con Quỷ, bắt nó phải thú tội và tiết lộ những sự thật kinh hoàng về nơi nó sống, cách nó kiểm soát tâm trí con người và cách để con người chiến thắng được nó. Khi đọc cuốn sách này, có thể bạn sẽ tự hỏi, cuộc trò chuyện này có thật không? Con Quỷ là có thật hay là một sản phẩm của trí tưởng tượng của Napoleon Hill. Nhưng quyền lựa chọn cách hiểu vấn đề là của bạn. Bởi lẽ cuối cùng, thông qua cuộc trò chuyện với Con Quỷ, Napoleon Hill đã cung cấp cho chúng ta chìa khóa để chiến thắng Con Quỷ trong cuộc sống riêng của mỗi người.",
            100_000, "https://cdn0.fahasa.com/media/catalog/product/8/9/8935086847817_1.jpg",
            4.8F,
            "Napoleon Hill", "NXB Tổng Hợp TPHCM")
        )
        list.add(
            SachModel(
                "Sach_7", "Đường Đến Thành Công - Road To Success",
            "Napoleon Hill (1883 – 1970) được mệnh danh là “Người đặt nền tảng cho môn khoa học Thành công”. Ông chính là người khơi nguồn cho dòng sách khơi dậy những tiềm năng của con người với hàng loạt những tác phẩm nổi tiếng như Law of Success (Quy luật của sự thành công), Think and Grow Rich (Cách nghĩ để thành công), The Magic Ladder of Success (Những nấc thang huyền bí dẫn tới thành công), Success through A Positive Mental Attitude (Thành công từ cách tư duy tích cực)… Những tư tưởng thể hiện trong các tác phẩm của ông đều hướng đến mục tiêu chung là nâng cao tầm phát triển của con người.",
            200_000, "https://cdn0.fahasa.com/media/catalog/product/d/u/duongdenhcong_bia-01.jpg",
            5.0F,
            "Napoleon Hill", "NXB Tổng Hợp TPHCM")
        )
        list.add(
            SachModel(
                "Sach_8", "Kỷ Luật Bản Thân Nền Tảng Cho Thành Công",
            "Cố tổng thống mỹ Benjamin Franklin từng nói: “Nếu thời gian là thứ đáng giá nhất, phí phạm thời gian hẳn phải là sự lãng phí ngông cuồng nhất.” Cuốn sách “Kỷ luật bản thân nền tảng cho thành công” sẽ mang đến cho các bạn các mẹo và kỹ thuật đã được chứng minh để giúp bạn xem xét và đánh giá việc quản lý thời gian của mình và áp dụng các phương pháp làm việc mới để cải thiện nó. Nó bao gồm các ý tưởng tiết kiệm thời gian tuyệt vời, các giải pháp thực tế và danh sách công việc phải xem xét, cùng với lời khuyên về: kiểm soát thủ tục giấy tờ; sắp xếp các email; ủy nhiệm và làm việc với những người khác; ưu tiên tập trung vào những vấn đề trọng tâm; sắp xếp và duy trì tổ chức.",
            150_000, "https://cdn0.fahasa.com/media/catalog/product/8/9/8935086847817_1.jpg",
            4.2F,
            "Patrick Forsyth", "NXB Lao Động")
        )
        list.add(
            SachModel(
                "Sach_9", "Dám Làm Giàu",
            "Với quyển sách này, người đọc sẽ thay đổi suy nghĩ làm giàu và tìm thấy con đường thành công nhanh, bền vững nhất.  Với tâm huyết và sứ mệnh chia sẻ để giúp người Việt thành công hơn nữa, Phạm Tuấn Sơn đã đưa ra những kinh nghiệm thực tế và kiến thức quý báu  trong cuốn sách này. Ông chia sẻ: “Tôi nghĩ mình cần chia sẻ những điều đã biết và làm được.",
            50_000, "https://cdn0.fahasa.com/media/flashmagazine/images/page_images/dam_lam_giau/2021_03_31_14_09_47_1-390x510.jpg",
            4.5F,
            "Damon Rahariades", "NXB Hồng Đức")
        )
        list.add(
            SachModel(
                "Sach_3", "Dạy Con Làm Giàu 02 - Sử Dụng Đồng Vốn",
            "Bạn đang đi học? Bạn vừa ra trường và chưa có việc làm? Bạn đang làm việc với mức lương ổn định? Bạn đang làm cho một công ty tư nhân tư nhân? Bạn là một chủ doanh nghiệp nhỏ? Bạn muốn được thoải mái về tiền bạc ? “Dạy con làm giàu tập 2” sẽ là bước khởi đầu cho bạn - những quan điểm mới về đồng vốn và cách sử dụng tiền hiệu quả.",
            120_000, "https://cdn0.fahasa.com/media/flashmagazine/images/page_images/day_con_lam_giau_02___su_dung_dong_von_tai_ban_2020/2020_06_05_10_31_13_1-390x510.jpg",
            4.6F,
            "Napoleon HillRobert T Kiyosaki, Sharon, L Le", "NXB Trẻ")
        )

        for (sach: SachModel in list){
            db.collection(Constant.SACH.TB_SACH)
                .document(sach.sachId)
                .set(sach)
                .addOnSuccessListener {
                    Log.d(TAG, "initSachModel: ${sach.name}")
                }
                .addOnFailureListener{
                    Log.e(TAG, "initSachModel: ${it.message}")
                }
        }
    }
}