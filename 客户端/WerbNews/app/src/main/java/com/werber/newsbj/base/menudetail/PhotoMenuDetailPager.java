package com.werber.newsbj.base.menudetail;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.werber.newsbj.R;
import com.werber.newsbj.base.BaseMenuDetailPager;
import com.werber.newsbj.bean.PhotosData;
import com.werber.newsbj.global.GlobalContants;
import com.werber.newsbj.utils.CacheUtils;
import com.werber.newsbj.utils.bitmap.MyBitmapUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

/**
 * 菜单详情页-组图
 */
public class PhotoMenuDetailPager extends BaseMenuDetailPager {


    private ListView lvPhoto;
    private GridView gvPhoto;
    private List<PhotosData.DataEntity.NewsEntity> mNewsList;
    private PhotoAdapter mPhotoAdapter;

    private ImageButton btnPhotoType;//组图切换按钮

    public PhotoMenuDetailPager(Activity activity, ImageButton imBtnPhotoType) {
        super(activity);

        this.btnPhotoType=imBtnPhotoType;

        btnPhotoType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePhotoType();
            }
        });
    }

    /**
     * 切换组图显示状态
     */
    private boolean isListPhotoType=true;

    private void changePhotoType() {
        if(isListPhotoType){
            btnPhotoType.setImageResource(R.mipmap.icon_pic_grid_type);
            lvPhoto.setVisibility(View.GONE);
            gvPhoto.setVisibility(View.VISIBLE);

            isListPhotoType=false;
        }else {
            btnPhotoType.setImageResource(R.mipmap.icon_pic_list_type);
            gvPhoto.setVisibility(View.GONE);
            lvPhoto.setVisibility(View.VISIBLE);

            isListPhotoType=true;
        }
    }

    @Override
    public View initView() {

        View view=View.inflate(mActivity, R.layout.menu_photo_pager, null);

        lvPhoto = (ListView) view.findViewById(R.id.lv_photo);
        gvPhoto = (GridView) view.findViewById(R.id.gv_photo);

        return view;
    }

    @Override
    public void initData() {

        String cache = CacheUtils.getCache(mActivity, GlobalContants.PHOTOS_URL);
        if(!TextUtils.isEmpty(cache)){
            parseData(cache);
        }
        getDataFromServer();
    }

    /**
     * 从服务器获取数据
     */
    private void getDataFromServer() {
        RequestParams params=new RequestParams(GlobalContants.PHOTOS_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                parseData(result);

                //设置缓存
                CacheUtils.setCache(mActivity,GlobalContants.PHOTOS_URL,result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFinished() {

            }
        });


    }

    /**
     * 解析从服务器获取的数据
     * @param result
     */
    private void parseData(String result) {
        Gson gson=new Gson();
        PhotosData data = gson.fromJson(result, PhotosData.class);

        mNewsList = data.data.news;

        if(mPhotoAdapter==null){
            mPhotoAdapter=new PhotoAdapter();
            lvPhoto.setAdapter(mPhotoAdapter);
            gvPhoto.setAdapter(mPhotoAdapter);
        }

    }

    /**
     * 图片数据适配器
     */
    class PhotoAdapter extends BaseAdapter{

//        private ImageOptions imageOptions;
        private MyBitmapUtils utils;//自定义的BitmapUtils

        public PhotoAdapter(){
//            //xUtils3的图片配置
//            imageOptions = new ImageOptions.Builder()
//                    .setLoadingDrawableId(R.mipmap.pic_item_list_default)//网络加载中的图片显示
//                    .build();
            utils=new MyBitmapUtils();//
        }

        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public PhotosData.DataEntity.NewsEntity getItem(int position) {
            return mNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if(convertView==null){
                convertView=View.inflate(mActivity,R.layout.list_photo_item,null);
                holder=new ViewHolder(convertView);

                convertView.setTag(holder);
            }else {
                holder= (ViewHolder) convertView.getTag();
            }

            PhotosData.DataEntity.NewsEntity item = getItem(position);

            holder.tvTltle.setText(item.title);

            //利用xUtils3加载网络图片
//            x.image().bind(holder.ivPic,GlobalContants.SERVER_URL+item.listimage,imageOptions);//传递imageView对象和图片地址
            utils.disPlay(holder.ivPic,GlobalContants.SERVER_URL+item.listimage);

            return convertView;
        }

        class ViewHolder{

            private ImageView ivPic;
            private TextView tvTltle;

            public ViewHolder(View view){
                ivPic= (ImageView) view.findViewById(R.id.iv_pic);
                tvTltle= (TextView) view.findViewById(R.id.tv_title);
            }

        }
    }
}
