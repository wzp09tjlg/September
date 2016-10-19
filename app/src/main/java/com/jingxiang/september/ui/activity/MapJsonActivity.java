package com.jingxiang.september.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jingxiang.september.R;
import com.jingxiang.september.network.parse.BaseBook;
import com.jingxiang.september.network.parse.BaseBookList;
import com.jingxiang.september.network.parse.EmptyBean;
import com.jingxiang.september.ui.base.BaseFragmentActivity;
import com.jingxiang.september.ui.widget.GlobalToast.GloableToast;
import com.jingxiang.september.util.CommonHelper;
import com.jingxiang.september.util.GsonUtil;
import com.jingxiang.september.util.LogUtil;

/**
 * Created by wu on 2016/9/27.
 */
public class MapJsonActivity extends BaseFragmentActivity implements
    View.OnClickListener
{
    /** View */
    private View viewTitle;
    private ImageView imgTitleBack;
    private TextView textTitle;
    private ImageView imgTitleMenu;

    private Button btnToJson;
    private Button btnFromJson;
    /** Data */
    private Context mContext;
    private String mTitle;
    /**************************************************/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_json);
        getExtral(getIntent());
        initViews();
    }

    private void getExtral(Intent intent){
        Bundle bundle = intent.getExtras();

        mTitle = bundle.getString("TITLE");
    }

    private void initViews(){
        initTitle();

        btnFromJson = $(R.id.btn_fromJson);
        btnToJson = $(R.id.btn_toJson);

        initData();
    }

    private void initTitle(){
        viewTitle = $(R.id.layout_title);
        imgTitleBack = $(R.id.title_left_img);
        textTitle = $(R.id.title_center_text);
        imgTitleMenu = $(R.id.title_right_img);

        imgTitleMenu.setVisibility(View.INVISIBLE);
        textTitle.setText(mTitle);
    }

    private void initData(){
        mContext = this;

        initListener();
    }

    private void initListener(){
        imgTitleBack.setOnClickListener(this);
        btnToJson.setOnClickListener(this);
        btnFromJson.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_left_img:
                finish();
                break;
            case R.id.btn_toJson:
                doConvertJson2Bean();
                break;
            case R.id.btn_fromJson:
                //doConvertJson2Bean1();
                doCoverttEmptyBean();
                break;
        }
    }

    private void doCoverttEmptyBean(){
        GsonUtil<EmptyBean> gsonUtil = new GsonUtil<>(EmptyBean.class);
        EmptyBean bean = gsonUtil.parse(tempEmpty);
        if(bean == null)
            LogUtil.e("bean is null");
        else{
            LogUtil.e("bean is not null bean:" + bean.toString());
            GloableToast.show(bean.toString());
        }


    }

    private void doConvertJson2Bean(){
        String tempData = CommonHelper.getJsonElement(tempJson,"data");
        if(TextUtils.isEmpty(tempData)) return;
        String tempJsonList = CommonHelper.getKeyNumJson(tempData);
        if(TextUtils.isEmpty(tempJsonList)) return;
        GsonUtil<BaseBookList> gsonUtil = new GsonUtil<BaseBookList>(BaseBookList.class);
        BaseBookList list =  gsonUtil.parse(tempJsonList);
        StringBuilder builder = new StringBuilder();
        int tempLen = list.size();
        for(int i=0;i<tempLen;i++){
            builder.append( (i+1) + "" + list.get(i) + "\n");
        }
        if(list == null)
            LogUtil.e("list is null");
        else {
            LogUtil.e("list is not null list:" + builder.toString());
            GloableToast.show(builder.toString());
        }
    }

    private void doConvertJson2Bean1() {
        String tempData = CommonHelper.getJsonElement(tempJson, "data");
        if (TextUtils.isEmpty(tempData)) return;
        String tempJsonList = CommonHelper.getKeyNumBeanJson(tempData);
        if (TextUtils.isEmpty(tempJsonList)) return;
        GsonUtil<BaseBook> gsonUtil = new GsonUtil<BaseBook>(BaseBook.class);
        BaseBook book = gsonUtil.parse(tempJsonList);

        if (book == null)
            LogUtil.e("list is null");
        else{
            LogUtil.e("list is not null book:" + book);
            GloableToast.show(book.toString());
        }
    }

    private String tempEmpty = "{\"res_code\":0,\"res_msg\":\"this is s test msg\",\"res_bdy\":[]}";

    private String tempJson = "{\"etag\":92373,\"data\":{\"5351483\":{\"book_id\":\"5351483\",\"is_vip\":\"Y\",\"paytype\":\"3\",\"price\":0,\"intro\":\"\\u8001\\u516c\\u548c\\u59d0\\u59d0\\u8f66\\u5077\\u60c5\\u6389\\u6cb3\\u91cc\\u4e86\\uff0c\\u4ea4\\u8b66\\u6253\\u7535\\u8bdd\\u6765\\u8ba9\\u5979\\u9001\\u4e24\\u5957\\u8863\\u670d\\u8fc7\\u53bb\\uff0c\\u5979\\u624d\\u5982\\u68a6\\u521d\\u9192\\uff0c\\u4e0d\\u662f\\u6240\\u6709\\u7684\\u6d6a\\u5b50\\u90fd\\u4f1a\\u56de\\u5934\\u3002\\r\\n\\u59d0\\u59d0\\u6709\\u5b55\\uff0c\\u4e3a\\u4e86\\u5a36\\u5979\\u8fdb\\u95e8\\uff0c\\u4ed6\\u4e0d\\u60dc\\u4eb2\\u624b\\u5c06\\u5979\\u9001\\u7ed9\\u522b\\u7684\\u7537\\u4eba\\u3002\\u800c\\u90a3\\u4e2a\\u7537\\u4eba\\uff0c\\u4e0d\\u4ec5\\u662f\\u4ed6\\u7684\\u56db\\u53d4\\uff0c\\u66f4\\u662f\\u5979\\u59d0\\u59d0\\u7684\\u5408\\u6cd5\\u4e08\\u592b\\u3002\\r\\n\\u652f\\u79bb\\u7834\\u788e\\u7684\\u5a5a\\u59fb\\uff0c\\u5979\\u62ff\\u7740\\u4e00\\u7eb8\\u79bb\\u5a5a\\u534f\\u8bae\\u4e66\\u5fc3\\u7070\\u610f\\u51b7\\u7684\\u79bb\\u5f00\\uff0c\\u8fce\\u4e0a\\u7684\\u5374\\u662f\\u53e6\\u4e00\\u4e2a\\u7537\\u4eba\\u8d1f\\u624b\\u800c\\u7acb\\u7684\\u8eab\\u5f71\\u3002\\r\\n\\u201c\\u7ec8\\u4e8e\\u79bb\\u5a5a\\u4e86\\uff1f\\u201d\\u7537\\u4eba\\u62ff\\u8d70\\u5979\\u624b\\u91cc\\u7684\\u79bb\\u5a5a\\u534f\\u8bae\\u4e66\\uff0c\\u4ed4\\u7ec6\\u7814\\u7a76\\u4e0a\\u9762\\u7684\\u6761\\u6b3e\\uff0c\\u786e\\u4fdd\\u5979\\u7684\\u6743\\u76ca\\u3002\\r\\n\\u201c\\u2026\\u2026\\u662f\\u3002\\u201d\\u5979\\u770b\\u7740\\u4ed6\\u6ee1\\u9762\\u7b11\\u610f\\uff0c\\u4f38\\u624b\\u6b32\\u593a\\u56de\\u534f\\u8bae\\u4e66\\u3002\\r\\n\\u4ed6\\u624b\\u81c2\\u4e00\\u8ba9\\uff0c\\u7b11\\u7740\\u5c06\\u5979\\u62e5\\u8fdb\\u6000\\u91cc\\uff0c\\u201c\\u4e5f\\u597d\\uff0c\\u5e76\\u4e0d\\u662f\\u6bcf\\u4e2a\\u7537\\u4eba\\u90fd\\u6709\\u6211\\u8fd9\\u79cd\\u548c\\u4f60\\u8fc7\\u4e00\\u8f88\\u5b50\\u7684\\u51b3\\u5fc3\\u548c\\u6bc5\\u529b\\u3002\\u4ee5\\u540e\\uff0c\\u4f60\\u5c31\\u548c\\u6211\\u76f8\\u4f9d\\u4e3a\\u547d\\u5427\\u3002\\u201d\\r\\n\\u5b8b\\u4f9d\\u8bfa\\uff1a\\u201c\\u2026\\u2026\\u201d\",\"title\":\"\\u4f60\\u66fe\\u662f\\u6211\\u552f\\u4e00\",\"updatetime\":\"1474612652\",\"s_bid\":\"5351483\",\"img\":\"http:\\/\\/vipbook.sinaedge.com\\/bookcover\\/pics\\/59\\/cover_54c430652f933a00bcb3626147d21be0.jpg\",\"status\":\"SERIES\",\"src\":\"websina\",\"sina_id\":\"\",\"last_chapter\":{\"chapter_id\":\"10278698\",\"title\":\"\\u7b2c210\\u7ae0 \\u54ac\\u6211\\uff0c\\u55ef\\uff1f\",\"is_vip\":\"Y\"},\"cate_name\":\"\\u7f8e\\u6587\\u5176\\u4ed6\",\"cate_id\":2,\"chapter_total\":209,\"chapter_num\":209,\"chapter_amount\":209,\"author\":\"\\u537f\\u7b71\",\"bid\":\"5351483\",\"cid\":\"\",\"createtime\":\"\",\"ios_app\":\"N\",\"isbuy\":\"U\",\"buy_type\":0,\"suite_id\":\"\",\"is_suite\":1,\"suite_name\":\"\",\"kind\":\"1\",\"index\":1},\"53350\":{\"book_id\":\"53350\",\"is_vip\":\"N\",\"paytype\":\"1\",\"price\":0,\"intro\":\"\\u4ec0\\u4e48\\u662f\\u6b66\\u6797\\u8d25\\u7c7b\\uff1f\\u4e3a\\u4e86\\u4e00\\u5df1\\u4e4b\\u79c1\\u6380\\u8d77\\u8165\\u98ce\\u8840\\u96e8\\uff0c\\u6b32\\u4e00\\u7edf\\u6c5f\\u6e56\\u800c\\u6740\\u4eba\\u5982\\u9ebb\\u7684\\uff1f\\u4e0d\\u5bf9\\uff0c\\u90a3\\u53eb\\u5978\\u96c4\\u3002\\u771f\\u6b63\\u7684\\u6b66\\u6797\\u8d25\\u7c7b\\u5c31\\u662f\\uff0c\\u5148\\u5929\\u6ca1\\u6709\\u751f\\u5f97\\u9aa8\\u9abc\\u7cbe\\u5947\\uff0c\\u4e0d\\u662f\\u767e\\u5e74\\u96be\\u5f97\\u4e00\\u9047\\u7684\\u7ec3\\u6b66\\u5947\\u624d\\uff0c\\u540e\\u5929\\u53c8\\u6ca1\\u6709\\u5f97\\u9047\\u540d\\u5e08\\uff0c\\u6216\\u662f\\u6361\\u672c\\u6b66\\u529f\\u79d8\\u7b08\\u81ea\\u5b66\\u6210\\u6750\\uff0c\\u66f4\\u6ca1\\u6709\\u6454\\u4e00\\u8de4\\u5403\\u4e0b\\u53bb\\u4e00\\u68f5\\u5343\\u5e74\\u7075\\u829d\\uff0c\\u4e07\\u5e74\\u4eba\\u53c2\\u6253\\u901a\\u4efb\\u7763\\u4e8c\\u8109\\uff0c\\u5f53\\u7136\\u4e5f\\u6ca1\\u6709\\u8df3\\u5d16\\u81ea\\u6740\\u5374\\u5728\\u5d16\\u5e95\\u9047\\u5230\\u4e2a\\u534a\\u6b7b\\u4e0d\\u6d3b\\u7684\\u9ad8\\u4eba\\uff0c\\u5c06\\u81ea\\u5df1\\u51e0\\u5341\\u5e74\\u529f\\u529b\\u767d\\u767d\\u76f8\\u9001\\uff0c\\u5916\\u52a0\\u9644\\u8d60\\u6b66\\u529f\\u79d8\\u7b08\\u4e00\\u672c\\uff0c\\u77ac\\u95f4\\u6210\\u4e3a\\u5185\\u5916\\u517c\\u4fee\\u7684\\u9ad8\\u624b\\u7684\\u90a3\\u79cd\\u4eba\\u3002\\u8fd9\\u79cd\\u4eba\\u65e2\\u4e0d\\u80fd\\u4e00\\u51fa\\u6c5f\\u6e56\\u5c31\\u5efa\\u529f\\u7acb\\u4e1a\\uff0c\\u6210\\u4e3a\\u6b66\\u6797\\u5947\\u8469\\uff0c\\u4e5f\\u4e0d\\u80fd\\u8dfb\\u8eab\\u9ad8\\u624b\\u4e4b\\u5217\\u5438\\u5f15\\u65e0\\u6570\\u7f8e\\u5973\\u6295\\u6000\\u9001\\u62b1\\uff0c\\u53ef\\u6c5f\\u6e56\\u4e0a\\u95ef\\u8361\\u7684\\u5927\\u591a\\u90fd\\u662f\\u8fd9\\u79cd\\u4eba\\uff0c\\u5c31\\u662f\\u4ed6\\u4eec\\u7ec4\\u6210\\u4e86\\u8fd9\\u4e2a\\u5149\\u602a\\u79bb\\u5947\\uff0c\\u5f15\\u4eba\\u5165\\u80dc\\u7684\\u6c5f\\u6e56\\u3002\",\"title\":\"\\u6b66\\u6797\\u8d25\\u7c7b\",\"updatetime\":\"1220800126\",\"s_bid\":\"53350\",\"img\":\"http:\\/\\/vipbook.sinaedge.com\\/bookcover\\/pics\\/102\\/cover_7682a935a0b81a5a34938efc75196e26.jpg\",\"status\":\"FINISH\",\"src\":\"websina\",\"sina_id\":\"\",\"last_chapter\":[],\"cate_name\":\"\\u6b66\\u4fa0\\u5c0f\\u8bf4\",\"cate_id\":5,\"chapter_total\":328,\"chapter_num\":328,\"chapter_amount\":328,\"author\":\"\\u6a58and\\u6a59\",\"bid\":\"53350\",\"cid\":\"\",\"createtime\":\"\",\"ios_app\":\"N\",\"isbuy\":\"U\",\"buy_type\":0,\"suite_id\":\"\",\"is_suite\":1,\"suite_name\":\"\",\"kind\":\"1\",\"index\":2},\"5350076\":{\"book_id\":\"5350076\",\"is_vip\":\"Y\",\"paytype\":\"3\",\"price\":0,\"intro\":\"\\u521a\\u5e2e\\u4e00\\u5b55\\u5987\\u751f\\u4ea7\\u5b8c\\uff0c\\u8fd8\\u6ca1\\u51fa\\u75c5\\u623f\\u5c31\\u88ab\\u4eba\\u7529\\u4e86\\u4e24\\u4e2a\\u54cd\\u4eae\\u7684\\u5df4\\u638c\\u3002\\r\\n\\u4e8b\\u540e\\u624d\\u77e5\\uff0c\\u6211\\u7adf\\u7136\\u4eb2\\u624b\\u63a5\\u751f\\u4e86\\u672a\\u5a5a\\u592b\\u7684\\u79c1\\u751f\\u5b50\\uff01\\r\\n\\u5a5a\\u793c\\u53d6\\u6d88\\uff0c\\u5bb6\\u5b85\\u4e0d\\u5b81\\uff0c\\u6211\\u6210\\u4e86\\u6068\\u5ac1\\u5973\\u3002\\r\\n\\u76f8\\u4eb2\\u76f8\\u4eb2\\u518d\\u76f8\\u4eb2\\uff0c\\u76f4\\u5230\\u4ed6\\u5c06\\u6211\\u4ece\\u4e00\\u573a\\u5c34\\u5c2c\\u7684\\u76f8\\u4eb2\\u5bb4\\u4e0a\\u89e3\\u6551\\u51fa\\u6765\\u3002\\r\\n\\u6211\\u626f\\u7740\\u4ed6\\u7684\\u80f3\\u818a\\u95ee\\u9053\\uff1a\\u201c\\u4f60\\u8fd8\\u7f3a\\u59bb\\u5b50\\u5417\\uff1f\\u201d\\r\\n\\u201c\\u7f3a\\u3002\\u201d\\r\\n\\u5df2\\u7ecf\\u65e0\\u529b\\u518d\\u6298\\u817e\\u7684\\u6211\\u548c\\u8fd9\\u4e2a\\u7b2c\\u4e8c\\u6b21\\u89c1\\u9762\\u5c31\\u8981\\u8ddf\\u6211\\u7ed3\\u5a5a\\u7684\\u7537\\u4eba\\u9886\\u4e86\\u8bc1\\u3002\\r\\n\\u4e0d\\u7ba1\\u4ed6\\u76ee\\u7684\\u5982\\u4f55\\uff0c\\u6211\\u53ea\\u60f3\\u56fe\\u4e00\\u65f6\\u5b89\\u5b81\\u3002\\r\\n\\u53ea\\u662f\\uff0c\\u5a5a\\u540e\\u91cd\\u91cd\\u5384\\u8fd0\\u5201\\u96be\\u63a5\\u8e35\\u800c\\u6765\\uff0c\\u662f\\u8c01\\u5728\\u80cc\\u540e\\u64cd\\u7eb5\\uff1f\\r\\n\\u5f85\\u4e00\\u5207\\u5c18\\u57c3\\u843d\\u5b9a\\uff0c\\u88ab\\u4ed6\\u653e\\u5728\\u5fc3\\u5c16\\u7684\\u524d\\u59bb\\u5f52\\u6765\\uff0c\\u6211\\u53c8\\u8be5\\u4f55\\u53bb\\u4f55\\u4ece\\uff1f\\r\\n\\u6700\\u7ec8\\uff0c\\u4e24\\u573a\\u7231\\u60c5\\uff0c\\u4e00\\u6bb5\\u5a5a\\u59fb\\u8017\\u5c3d\\u4e86\\u6211\\u6240\\u6709\\u7684\\u70ed\\u60c5\\uff0c\\u51b3\\u7136\\u8f6c\\u8eab\\uff0c\\u201c\\u6c5f\\u58a8\\u8a00\\uff0c\\u6211\\u7d2f\\u4e86\\uff0c\\u4ee5\\u540e\\u518d\\u4e5f\\u4e0d\\u89c1\\u3002\\u201d\\r\\n\\u8bb0\\u5f97\\u90a3\\u5929\\uff0c\\u9633\\u5149\\u523a\\u773c\\uff0c\\u4f4e\\u5934\\u95f4\\uff0c\\u624b\\u629a\\u4e0a\\u5c0f\\u8179\\u3002\\r\\n\\u5b64\\u72ec\\u7ec8\\u8001\\u4f1a\\u662f\\u6211\\u6700\\u540e\\u7684\\u7ed3\\u5c40\\u5417\\uff1f\\r\\n\\u7247\\u6bb5\\uff1a\\u5b8b\\u5c0f\\u6eaa\\uff1a\\u4f60\\u89c9\\u5f97\\u6211\\u4eec\\u8fd9\\u6837\\u8349\\u8349\\u7ed3\\u5a5a\\u5bf9\\u5417\\uff1f\\r\\n\\u6c5f\\u58a8\\u8a00\\uff1a\\u4f60\\u76f8\\u4fe1\\u4e00\\u89c1\\u949f\\u60c5\\u8fd8\\u662f\\u65e5\\u4e45\\u751f\\u60c5\\uff1f\\r\\n\\u5b8b\\u5c0f\\u6eaa\\uff1a\\u65e5\\u4e45\\u751f\\u60c5\\u3002\\r\\n\\u6c5f\\u58a8\\u8a00\\uff1a\\u90a3\\u4e0d\\u5c31\\u5bf9\\u4e86\\uff0c\\u591a\\u505a\\u505a\\u611f\\u60c5\\u5c31\\u589e\\u52a0\\u4e86\\u3002\\r\\n\\u67d0\\u5973\\u62b1\\u4f4f\\u80f8\\u524d\\u72c2\\u543c\\u4e00\\u58f0\\uff1a\\u6c5f\\u58a8\\u8a00\\uff0c\\u4f60\\u6d41\\u6c13\\uff01\\r\\n\\u7537\\u4eba\\u4f18\\u96c5\\u7684\\u7aef\\u8d77\\u9762\\u524d\\u7684\\u5496\\u5561\\u8f7b\\u555c\\u53e3\\uff0c\\u6d45\\u7b11\\u4e0d\\u8bed\\uff0c\\u7231\\u60c5\\u91cc\\u9762\\u603b\\u6709\\u4e00\\u4e2a\\u8981\\u5148\\u800d\\u6d41\\u6c13\\uff0c\\u4e0d\\u7136\\u592a\\u65e0\\u8da3\\u3002\",\"title\":\"\\u4f60\\u8bf4\\u8fc7\\uff0c\\u6211\\u4fe1\\u8fc7\",\"updatetime\":\"1458550483\",\"s_bid\":\"5350076\",\"img\":\"http:\\/\\/vipbook.sinaedge.com\\/bookcover\\/pics\\/188\\/cover_4469fe53f3d87ac6c96e27a60ca5fe44.jpg\",\"status\":\"FINISH\",\"src\":\"websina\",\"sina_id\":\"\",\"last_chapter\":[],\"cate_name\":\"\\u7f8e\\u6587\\u5176\\u4ed6\",\"cate_id\":2,\"chapter_total\":325,\"chapter_num\":325,\"chapter_amount\":325,\"author\":\"\\u9c7c\\u53ef\\u53ef\",\"bid\":\"5350076\",\"cid\":\"\",\"createtime\":\"\",\"ios_app\":\"N\",\"isbuy\":\"U\",\"buy_type\":0,\"suite_id\":\"\",\"is_suite\":1,\"suite_name\":\"\",\"kind\":\"1\",\"index\":3},\"5363581\":{\"book_id\":\"5363581\",\"is_vip\":\"Y\",\"paytype\":\"3\",\"price\":0,\"intro\":\"\\u3000\\u3000\\u7535\\u68af\\u91cc\\u7684\\u7981\\u5fcc\\uff1a\\r\\n1\\uff1a\\u7535\\u68af\\u6253\\u5f00\\u95e8\\uff0c\\u800c\\u4f60\\u770b\\u5230\\u7535\\u68af\\u91cc\\u7684\\u4eba\\u90fd\\u4f4e\\u5934\\uff0c\\u5e76\\u4e14\\u5411\\u4e0a\\u62ac\\u773c\\u770b\\u4f60\\u7684\\u65f6\\u5019\\uff0c\\u5343\\u4e07\\u4e0d\\u8981\\u8d70\\u8fdb\\u53bb\\u3002\\r\\n2 \\uff1a\\u5343\\u4e07\\u4e0d\\u8981\\u5728\\u7535\\u68af\\u91cc\\u7167\\u955c\\u5b50\\u3002\\r\\n3 \\uff1a\\u5728\\u7535\\u68af\\u4e2d\\u6709\\u4eba\\u95ee\\u65f6\\u95f4\\uff0c\\u5343\\u4e07\\u4e0d\\u8981\\u56de\\u7b54\\u3002 \\r\\n\\u3000\\u3000\\u621114\\u5c81\\u8fdb\\u5e1d\\u90fd\\u5f53\\u7ef4\\u4fee\\u5de5\\uff0c\\u5e72\\u4e86\\u5341\\u5e74\\u4e86\\uff0c\\u8fd0\\u6c14\\u4e0d\\u597d\\uff0c\\u7ecf\\u5386\\u4e86\\u4e00\\u4e9b\\u80ae\\u810f\\u7684\\u4e8b\\u60c5\\uff0c\\u73b0\\u5728\\u60f3\\u5199\\u4e0b\\u6765\\uff0c\\u8ba9\\u5927\\u5bb6\\u591a\\u8b66\\u60d5\\u4e00\\u4e9b\\uff0c\\u8fd9\\u4e2a\\u4e16\\u754c\\u4e0a\\u5371\\u9669\\u5f88\\u591a\\uff0c\\u5e74\\u8f7b\\u4eba\\u5207\\u8bb0\\u4e0d\\u8981\\u778e\\u73a9\\u3002\\r\\n\\u7edd\\u5bf9\\u771f\\u5b9e\\u7684\\u7ecf\\u5386\\uff0c\\u544a\\u8bc9\\u4f60\\u7535\\u68af\\u4e0d\\u4e3a\\u4eba\\u77e5\\u7684\\u4e00\\u9762...\",\"title\":\"\\u7535\\u68af\\u6b7b\\u5fcc\",\"updatetime\":\"1463550773\",\"s_bid\":\"5363581\",\"img\":\"http:\\/\\/vipbook.sinaedge.com\\/cimg\\/216\\/cfcb6656c9ec911a8d120320d5ed0efc.jpg\",\"status\":\"FINISH\",\"src\":\"websina\",\"sina_id\":\"\",\"last_chapter\":[],\"cate_name\":\"\\u7f8e\\u6587\\u5176\\u4ed6\",\"cate_id\":9,\"chapter_total\":575,\"chapter_num\":575,\"chapter_amount\":575,\"author\":\"QD\",\"bid\":\"5363581\",\"cid\":\"\",\"createtime\":\"\",\"ios_app\":\"N\",\"isbuy\":\"U\",\"buy_type\":0,\"suite_id\":\"\",\"is_suite\":1,\"suite_name\":\"\",\"kind\":\"1\",\"index\":4},\"5350894\":{\"book_id\":\"5350894\",\"is_vip\":\"Y\",\"paytype\":\"3\",\"price\":0,\"intro\":\"\\u9648\\u950b\\u4f5c\\u4e3a\\u4f4e\\u8c03\\u7684\\u4fdd\\u5b89\\uff0c\\u4ed6\\u6210\\u529f\\u5417\\uff1f\\r\\n\\u5f88\\u6210\\u529f\\uff0c\\u4e3a\\u4ec0\\u4e48\\uff1f\\r\\n\\u9ad8\\u51b7\\u7684\\u7f8e\\u5973\\u603b\\u88c1\\u4e0e\\u4ed6\\u7b7e\\u8ba2\\u5951\\u7ea6\\uff0c\\u6210\\u4e3a\\u6709\\u540d\\u65e0\\u5b9e\\u7684\\u5951\\u7ea6\\u592b\\u59bb\\u3002\\r\\n\\u53ef\\u601c\\u7684\\u9648\\u950b\\uff0c\\u53ea\\u5f97\\u5916\\u51fa\\u89c5\\u98df\\u3002\\r\\n\\u201c\\u6211\\u63a5\\u53d7\\u4e27\\u5076\\uff0c\\u4f46\\u4e0d\\u63a5\\u53d7\\u51fa\\u8f68\\u3002\\u201d\\r\\n\\u5f81\\u670d\\u5982\\u6b64\\u9738\\u9053\\u9ad8\\u51b7\\u7684\\u603b\\u88c1\\u8001\\u5a46\\uff0c\\u8eab\\u4e3a\\u7eaf\\u7237\\u4eec\\u7684\\u9648\\u950b\\u8868\\u793a\\u5f88\\u5174\\u594b\\u2026\\u2026\",\"title\":\"\\u7f8e\\u5973\\u603b\\u88c1\\u7684\\u5168\\u80fd\\u5175\\u738b\",\"updatetime\":\"1474880146\",\"s_bid\":\"5350894\",\"img\":\"http:\\/\\/vipbook.sinaedge.com\\/bookcover\\/pics\\/238\\/cover_531f2cf82cb5699a89863acbb86c7910.jpg\",\"status\":\"SERIES\",\"src\":\"websina\",\"sina_id\":\"\",\"last_chapter\":{\"chapter_id\":\"10283903\",\"title\":\"\\u7b2c400\\u7ae0\\u3000\\u522b\\u5885\\u70ed\\u95f9\\u4e86\",\"is_vip\":\"Y\"},\"cate_name\":\"\\u7f8e\\u6587\\u5176\\u4ed6\",\"cate_id\":1,\"chapter_total\":396,\"chapter_num\":396,\"chapter_amount\":396,\"author\":\"\\u4efb\\u6027\\u7684\\u72ee\\u5b50\",\"bid\":\"5350894\",\"cid\":\"\",\"createtime\":\"\",\"ios_app\":\"N\",\"isbuy\":\"U\",\"buy_type\":0,\"suite_id\":\"\",\"is_suite\":1,\"suite_name\":\"\",\"kind\":\"1\",\"index\":5},\"5352541\":{\"book_id\":\"5352541\",\"is_vip\":\"Y\",\"paytype\":\"3\",\"price\":0,\"intro\":\"\\u4e16\\u754c\\u7b2c\\u4e00\\u7684\\u795e\\u79d8\\u6740\\u624b\\u9f99\\u9b44\\uff0c\\u5728\\u5f02\\u56fd\\u4ed6\\u4e61\\u8f6c\\u884c\\u5356\\u8d77\\u4e86\\u714e\\u997c\\u679c\\u5b50\\uff0c\\u8fd8\\u88ab\\u5e08\\u7236\\u903c\\u7740\\u56de\\u56fd\\u53bb\\u8ddf\\u7d20\\u672a\\u8c0b\\u9762\\u7684\\u5973\\u4eba\\u76f8\\u4eb2\\u3002\\u4e0d\\u8fc7\\u542c\\u8bf4\\u8fd9\\u5973\\u4eba\\u4e0d\\u4f46\\u8c8c\\u7f8e\\u5982\\u82b1\\u3001\\u503e\\u56fd\\u503e\\u57ce\\uff0c\\u800c\\u4e14\\u8fd8\\u662f\\u597d\\u83b1\\u575e\\u6700\\u7099\\u624b\\u53ef\\u70ed\\u7684\\u8d85\\u7ea7\\u5973\\u661f\\uff0c\\u540c\\u65f6\\u66f4\\u662f\\u4e00\\u4e2a\\u5750\\u62e5\\u8d44\\u4ea7\\u51e0\\u5341\\u4ebf\\u5927\\u96c6\\u56e2\\u7684\\u767d\\u5bcc\\u7f8e\\u3002\\r\\u65e2\\u7136\\u4e0d\\u80fd\\u9690\\u59d3\\u57cb\\u540d\\u8fc7\\u4f4e\\u8c03\\u7684\\u751f\\u6d3b\\uff0c\\u90a3\\u5c31\\u91cd\\u65b0\\u5d1b\\u8d77\\u91ca\\u653e\\u51fa\\u6700\\u8000\\u773c\\u7684\\u5149\\u8292\\u3002\\r\\u4ece\\u6b64\\uff0c\\u54ea\\u91cc\\u6709\\u6781\\u54c1\\u7f8e\\u4eba\\uff0c\\u54ea\\u91cc\\u5c31\\u662f\\u4ed6\\u72e9\\u730e\\u7684\\u6218\\u573a\\u3002\",\"title\":\"\\u6843\\u8fd0\\u5175\\u738b\",\"updatetime\":\"1474359941\",\"s_bid\":\"5352541\",\"img\":\"http:\\/\\/vipbook.sinaedge.com\\/bookcover\\/pics\\/93\\/cover_26c891dfe8a0d136dbd7b42288048fec.jpg\",\"status\":\"SERIES\",\"src\":\"websina\",\"sina_id\":\"\",\"last_chapter\":{\"chapter_id\":\"10321145\",\"title\":\"\\u7b2c1710\\u7ae0\\u3000\\u98ce\\u4e4b\\u5e7d\\u7075\",\"is_vip\":\"Y\"},\"cate_name\":\"\\u7f8e\\u6587\\u5176\\u4ed6\",\"cate_id\":1,\"chapter_total\":1709,\"chapter_num\":1709,\"chapter_amount\":1709,\"author\":\"\\u9752\\u950b\",\"bid\":\"5352541\",\"cid\":\"\",\"createtime\":\"\",\"ios_app\":\"N\",\"isbuy\":\"U\",\"buy_type\":0,\"suite_id\":\"\",\"is_suite\":1,\"suite_name\":\"\",\"kind\":\"1\",\"index\":6},\"5307446\":{\"book_id\":\"5307446\",\"is_vip\":\"Y\",\"paytype\":\"3\",\"price\":0,\"intro\":\"1938\\u5e74\\u548c1943\\u5e74\\uff0c\\u5e0c\\u7279\\u52d2\\u66fe\\u6d3e\\u52a9\\u624b\\u5e0c\\u59c6\\u83b1\\u4e24\\u6b21\\u5e26\\u961f\\u6df1\\u5165\\u897f\\u85cf\\uff1b\\u5728\\u65b0\\u4e2d\\u56fd\\u6210\\u7acb\\u4e4b\\u521d\\uff0c\\u65af\\u5927\\u6797\\u66fe\\u6d3e\\u82cf\\u8054\\u4e13\\u5bb6\\u56e2\\u524d\\u540e\\u4e94\\u6b21\\u8003\\u5bdf\\u897f\\u85cf\\uff0c\\u4ed6\\u4eec\\u7684\\u79d8\\u5bc6\\u884c\\u52a8\\u610f\\u5473\\u6df1\\u8fdc\\uff0c\\u6ca1\\u6709\\u4eba\\u77e5\\u9053\\u4ed6\\u4eec\\u7684\\u771f\\u5b9e\\u76ee\\u7684\\u3002\\u591a\\u5e74\\u4e4b\\u540e\\uff0c\\u8eab\\u5728\\u7f8e\\u56fd\\u5bbe\\u5915\\u6cd5\\u5c3c\\u4e9a\\u5dde\\u7684\\u85cf\\u7352\\u4e13\\u5bb6\\u5353\\u6728\\u5f3a\\u5df4\\u7a81\\u7136\\u6536\\u5230\\u4e00\\u4e2a\\u964c\\u751f\\u4eba\\u9001\\u6765\\u7684\\u4fe1\\u5c01\\uff0c\\u4fe1\\u5c01\\u91cc\\u88c5\\u7740\\u4e24\\u5f20\\u7167\\u7247\\uff0c\\u7167\\u7247\\u4e0a\\u60ca\\u73b0\\u7684\\u8fdc\\u53e4\\u795e\\u517d\\uff0c\\u4fc3\\u4f7f\\u5353\\u6728\\u5f3a\\u5df4\\u53ca\\u5bfc\\u5e08\\u3001\\u4e16\\u754c\\u72ac\\u7c7b\\u5b66\\u4e13\\u5bb6\\u65b9\\u65b0\\u6559\\u6388\\u4eb2\\u8d74\\u897f\\u85cf\\u3002\\u4ed6\\u4eec\\u5728\\u8c03\\u67e5\\u8fc7\\u7a0b\\u4e2d\\u9707\\u60ca\\u5730\\u53d1\\u73b0\\uff0c\\u7167\\u7247\\u4e0a\\u7684\\u52a8\\u7269\\u7adf\\u7136\\u548c\\u5e15\\u5df4\\u62c9\\u795e\\u5e99\\u6709\\u5173\\u2026\\u2026\\u4e0d\\u4e45\\u4e4b\\u540e\\u3002\\u4e00\\u652f\\u7531\\u7279\\u79cd\\u5175\\u3001\\u8003\\u53e4\\u5b66\\u5bb6\\u3001\\u751f\\u7269\\u5b66\\u5bb6\\u3001\\u5bc6\\u4fee\\u9ad8\\u624b\\u7b49\\u5404\\u8272\\u4eba\\u7269\\u7ec4\\u6210\\u7684\\u795e\\u79d8\\u79d1\\u8003\\u961f\\uff0c\\u6084\\u6084\\u4ece\\u897f\\u85cf\\u51fa\\u53d1\\uff0c\\u5f00\\u59cb\\u4e86\\u4e00\\u573a\\u7a7f\\u8d8a\\u5168\\u7403\\u751f\\u6b7b\\u7981\\u5730\\u7684\\u63a2\\u9669\\u4e4b\\u65c5\\uff0c\\u4ed6\\u4eec\\u8981\\u8ffd\\u5bfb\\u85cf\\u4f20\\u4f5b\\u6559\\u5343\\u5e74\\u9690\\u79d8\\u5386\\u53f2\\u7684\\u771f\\u76f8\\u2026\\u2026\",\"title\":\"\\u85cf\\u5730\\u5bc6\\u7801\",\"updatetime\":\"1387449646\",\"s_bid\":\"5307446\",\"img\":\"http:\\/\\/vipbook.sinaedge.com\\/bookcover\\/pics\\/54\\/cover_9b64bb29901619b6c47d0c811a0b5fac.jpg\",\"status\":\"FINISH\",\"src\":\"websina\",\"sina_id\":\"\",\"last_chapter\":[],\"cate_name\":\"\\u7f8e\\u6587\\u5176\\u4ed6\",\"cate_id\":27,\"chapter_total\":302,\"chapter_num\":302,\"chapter_amount\":302,\"author\":\"\\u4f55\\u9a6c\",\"bid\":\"5307446\",\"cid\":\"\",\"createtime\":\"\",\"ios_app\":\"N\",\"isbuy\":\"U\",\"buy_type\":0,\"suite_id\":\"\",\"is_suite\":1,\"suite_name\":\"\",\"kind\":\"1\",\"index\":7},\"5349321\":{\"book_id\":\"5349321\",\"is_vip\":\"Y\",\"paytype\":\"3\",\"price\":0,\"intro\":\"\\u6d1b\\u5929,\\u534e\\u590f\\u795e\\u79d8\\u519b\\u4e8b\\u90e8\\u95e8\\u7684\\u6700\\u5f3a\\u5229\\u5203\\uff0c,\\u9488\\u5bf9\\u56fd\\u5185\\u56fd\\u9645\\u654c\\u5bf9\\u52bf\\u529b\\u8fdb\\u884c\\u4e86\\u6050\\u6016\\u7684\\u6253\\u51fb\\uff0c,\\u5076\\u7136\\u4e00\\u6b21\\u610f\\u5916,\\u4ed6\\u5931\\u53bb\\u4e86\\u6700\\u597d\\u7684\\u5144\\u5f1f\\uff0c\\u4e3a\\u4e86\\u7167\\u987e\\u597d\\u5144\\u5f1f\\u7684\\u4eb2\\u4eba\\uff0c\\u4ed6\\u4e00\\u4e2a\\u4eba\\u6765\\u5230\\u4e86\\u4e1c\\u660c\\u5e02\\u3002\\u8c01\\u77e5\\u9053\\u5144\\u5f1f\\u7684\\u5973\\u4eba\\u7adf\\u662f\\u591c\\u603b\\u4f1a\\u7684\\u98ce\\u60c5\\u5927\\u7f8e\\u5973,\\u968f\\u540e\\u5728\\u4e00\\u573a\\u5730\\u4e0b\\u52bf\\u529b\\u7684\\u4e89\\u6597\\u5f53\\u4e2d,\\u5bb9\\u59d0\\u88ab\\u8fb1,\\u6d1b\\u5929\\u4e00\\u6012\\u4e3a\\u7ea2\\u989c\\u2026\\u2026\",\"title\":\"\\u900d\\u9065\\u5175\\u738b\",\"updatetime\":\"1458110195\",\"s_bid\":\"5349321\",\"img\":\"http:\\/\\/vipbook.sinaedge.com\\/cimg\\/253\\/24e81f7018d3dffe996f2ca9e48bdd21.jpg\",\"status\":\"SERIES\",\"src\":\"websina\",\"sina_id\":\"\",\"last_chapter\":{\"chapter_id\":\"10278266\",\"title\":\"\\u7b2c871\\u7ae0\\u3000\\u54ed\\u8d27\\u7684\\u7535\\u8bdd\",\"is_vip\":\"Y\"},\"cate_name\":\"\\u7f8e\\u6587\\u5176\\u4ed6\",\"cate_id\":1,\"chapter_total\":871,\"chapter_num\":871,\"chapter_amount\":871,\"author\":\"\\u6697\\u591c\\u884c\\u8d70\",\"bid\":\"5349321\",\"cid\":\"\",\"createtime\":\"\",\"ios_app\":\"N\",\"isbuy\":\"U\",\"buy_type\":0,\"suite_id\":\"\",\"is_suite\":1,\"suite_name\":\"\",\"kind\":\"1\",\"index\":8},\"5348944\":{\"book_id\":\"5348944\",\"is_vip\":\"Y\",\"paytype\":\"3\",\"price\":0,\"intro\":\"\\u4ed6\\u662f\\u53f1\\u54a4\\u98ce\\u4e91\\u7684\\u96c6\\u56e2\\u9996\\u5e2d\\uff0c\\u53c8\\u662f\\u51b7\\u9177\\u9738\\u9053\\u7684\\u94bb\\u77f3\\u5355\\u8eab\\u6c49\\u3002\\u5979\\u8eab\\u4efd\\u8ff7\\u79bb\\uff0c\\u672c\\u53bb\\u6349\\u5978\\uff0c\\u5374\\u610f\\u5916\\u5c06\\u94bb\\u77f3\\u9996\\u5e2d\\u5403\\u5e72\\u62b9\\u51c0\\u8fd8\\u60f3\\u4e24\\u6e05\\u3002\\u7136\\u800c\\uff0c\\u88ab\\u5403\\u8005\\u610f\\u6b32\\u672a\\u5c3d\\u3002\\u4ed6\\u6b65\\u6b65\\u4e3a\\u8425\\uff0c\\u5f15\\u5979\\u5165\\u5c40\\u3002\\u5979\\u7cbe\\u4e8e\\u7b97\\u8ba1\\uff0c\\u72e1\\u733e\\u5982\\u732b\\u3002\\u4e3a\\u4e86\\u6293\\u4f4f\\u8fd9\\u53ea\\u72e1\\u733e\\u7684\\u732b\\uff0c\\u4ed6\\u65e0\\u8282\\u64cd\\u65e0\\u4e0b\\u9650\\uff0c\\u52bf\\u5fc5\\u8981\\u5c06\\u732b\\u6251\\u5012\\uff0c\\u60b2\\u5267\\u53d1\\u751f\\uff0c\\u4ed6\\u88ab\\u53cd\\u6251\\u3002\",\"title\":\"\\u840c\\u59bb\\u6765\\u88ad\\uff1a\\u9996\\u5e2d\\u6b65\\u6b65\\u60ca\\u5a5a\",\"updatetime\":\"1464877593\",\"s_bid\":\"5348944\",\"img\":\"http:\\/\\/vipbook.sinaedge.com\\/cimg\\/149\\/20ea2fc1c161c8c609f8bcc6f87cfb62.jpg\",\"status\":\"SERIES\",\"src\":\"websina\",\"sina_id\":\"\",\"last_chapter\":{\"chapter_id\":\"10290822\",\"title\":\"\\u7b2c624\\u7ae0 \\u5634\\u4e0b\\u7559\\u60c5\",\"is_vip\":\"Y\"},\"cate_name\":\"\\u7f8e\\u6587\\u5176\\u4ed6\",\"cate_id\":2,\"chapter_total\":623,\"chapter_num\":623,\"chapter_amount\":623,\"author\":\"\\u5c0f\\u98de\\u4fa0\",\"bid\":\"5348944\",\"cid\":\"\",\"createtime\":\"\",\"ios_app\":\"N\",\"isbuy\":\"U\",\"buy_type\":0,\"suite_id\":\"\",\"is_suite\":1,\"suite_name\":\"\",\"kind\":\"1\",\"index\":9},\"5351500\":{\"book_id\":\"5351500\",\"is_vip\":\"Y\",\"paytype\":\"3\",\"price\":0,\"intro\":\"\\u9b3c\\u8fd9\\u4e1c\\u897f\\uff0c\\u5f88\\u591a\\u4eba\\u4fe1\\uff0c\\u4e5f\\u6709\\u5f88\\u591a\\u4eba\\u4e0d\\u4fe1\\u3002\\u53bb\\u6cf0\\u56fd\\u4e4b\\u524d\\u6211\\u6839\\u672c\\u4e0d\\u4fe1\\uff0c\\u53ef\\u540e\\u6765\\u6211\\u7adf\\u5f00\\u5e97\\u5356\\u8d77\\u4e86\\u4f5b\\u724c\\u2026\\u2026\\u4ec0\\u4e48\\uff0c\\u4f60\\u5c45\\u7136\\u4e0d\\u77e5\\u9053\\u4f5b\\u724c\\u91cc\\u6709\\u9b3c\\uff1f\\r\\n\\r\\n\\u6bcf\\u5929\\u4e09\\u66f4\\uff08\\u65e9\\u4e0a8\\u70b9\\u3001\\u4e2d\\u534812\\u70b9\\u3001\\u665a\\u4e0a8\\u70b9\\uff09\\u3002\\u56e0\\u5de5\\u4f5c\\u3001\\u5bb6\\u5ead\\u548c\\u7cbe\\u529b\\u6709\\u9650\\uff0c\\u4ece2016\\u5e74\\u5f00\\u59cb\\uff0c\\u6240\\u6253\\u8d4f\\u7684\\u7389\\u4f69\\u548c\\u7687\\u51a0\\u53ea\\u80fd\\u914c\\u60c5\\u6162\\u6162\\u52a0\\u66f4\\uff0c\\u5982\\u4e0d\\u80fd\\u63a5\\u53d7\\uff0c\\u8fd8\\u8bf7\\u5c06\\u94f6\\u5b50\\u7559\\u7740\\u8ba2\\u9605\\u5427\\uff0c\\u9664\\u975e\\u60a8\\u6253\\u8d4f\\u53ea\\u4e3a\\u8868\\u8fbe\\u5bf9\\u672c\\u4e66\\u548c\\u4f5c\\u8005\\u7684\\u7231\\u3002\\u3002\\u3002\\r\\n--------------------------------------------------------\\r\\n\\u9b3c\\u5e97\\u4e3b\\u65b0\\u6d6a\\u5fae\\u535a\\uff1ahttp:\\/\\/weibo.com\\/u\\/5262410967\\uff08\\u6216\\u641c\\uff1a\\u9b3c\\u5e97\\u4e3b\\u7530\\u4e03\\uff09\\u817e\\u8bafQQ\\u53f7\\uff1a778524365\\r\\n\\u9b3c\\u5e97\\u4e3b\\u5fae\\u4fe1\\u516c\\u4f17\\u53f7\\uff1aguidianzhutianqi\\r\\n\\u65b0\\u6d6a\\u672c\\u4e66\\u5fae\\u8bdd\\u9898\\uff1ahttp:\\/\\/weibo.com\\/p\\/100202read8364418?from=\\r\\n--------------------------------------------------------\",\"title\":\"\\u6211\\u5728\\u6cf0\\u56fd\\u5356\\u4f5b\\u724c\\u7684\\u90a3\\u51e0\\u5e74\",\"updatetime\":\"1473213861\",\"s_bid\":\"5351500\",\"img\":\"http:\\/\\/vipbook.sinaedge.com\\/bookcover\\/pics\\/76\\/cover_7b6596905ed642c9cbafebadda33cea0.jpg\",\"status\":\"FINISH\",\"src\":\"websina\",\"sina_id\":\"\",\"last_chapter\":[],\"cate_name\":\"\\u7f8e\\u6587\\u5176\\u4ed6\",\"cate_id\":9,\"chapter_total\":1076,\"chapter_num\":1076,\"chapter_amount\":1076,\"author\":\"\\u9b3c\\u5e97\\u4e3b\",\"bid\":\"5351500\",\"cid\":\"\",\"createtime\":\"\",\"ios_app\":\"N\",\"isbuy\":\"U\",\"buy_type\":0,\"suite_id\":\"\",\"is_suite\":1,\"suite_name\":\"\",\"kind\":\"1\",\"index\":10}},\"status\":{\"code\":0,\"msg\":\"\\u6210\\u529f\"}}";
}
