# TakeOutN
外卖

外卖项目胡服务端代码：

    https://gitee.com/nangongyibin/Java_TakeOut.git


# 外卖项目 #
## 1 拷贝布局以及相关drawable文件 ##
## 2 引入material design相关jar包 ##
>项目中需要用到tabLayout,所以需要导入相关jar包
	
	导入包      implementation 'com.android.support:design:27.1.1'
## 3 项目中使用Butterknife ##
>使用ButterKnife减少findViewById
	添加插件,plug中搜索ButterKnife插件

	app工程build.gradle添加如下代码
	apply plugin: 'com.jakewharton.butterknife'
	
	dependencies {
	    .....
	    implementation 'com.jakewharton:butterknife:8.5.1'
	    annotationProcessor 'com.jakewharton:butterknife-compiler:8.5.1'
	    .....
	}
## 4 首页MainActivity编写 ##
### 4.1 构建多个Fragment ###

    
    private void initFragment() {
        fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(new HomeFragment());
        fragmentArrayList.add(new OrderFragment());
        fragmentArrayList.add(new UserFragment());
        fragmentArrayList.add(new MoreFragment());
    }

### 4.2 底部每个tab注册点击事件 ###

    
    private void initClick() {
        int childCount = mainBottomeSwitcherContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAtView = mainBottomeSwitcherContainer.getChildAt(i);
            childAtView.setOnClickListener(this);
        }
    }

### 4.3 点击事件中修改UI效果 ###

    
    @Override
    public void onClick(View v) {
        //v底部现行布局的索引位置
        int indexOfChild = mainBottomeSwitcherContainer.indexOfChild(v);
        //点中哪个帧布局,这个帧布局中的图片就需要变为蓝色(不可用)
        changeUI(indexOfChild);
        //顶部的fragment页面内容需要切换
        changeFragment(indexOfChild);
    }

    private void changeFragment(int indexOfChild) {
        //需要切换到哪个fragment显示
        Fragment fragment = fragmentArrayList.get(indexOfChild);
        //替换帧布局内部内容
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, fragment).commit();
    }

    private void changeUI(int indexOfChild) {
        //蓝色--->不可用
        int childCount = mainBottomeSwitcherContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childFrameLayout = mainBottomeSwitcherContainer.getChildAt(i);
            //循环遍历到了选中的索引位置
            if (i == indexOfChild) {
                //点中的帧布局,内部的图片和文字都需要变蓝色
                setEnable(childFrameLayout, false);
            } else {
                //未点中的帧布局,内部的图片和文字都需要变黑色
                setEnable(childFrameLayout, true);
            }
        }
    }

    /**
     * @param childFrameLayout 帧布局
     * @param b                是否可用
     *                         让帧布局内部的控件和自身都是(可用)不可用的状态
     */
    private void setEnable(View childFrameLayout, boolean b) {
        childFrameLayout.setEnabled(b);
        if (childFrameLayout instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) childFrameLayout).getChildCount(); i++) {
                View childAt = ((ViewGroup) childFrameLayout).getChildAt(i);
                childAt.setEnabled(b);
            }
        }
    }

### 4.4 底部tab选中未选中效果 ###

    <?xml version="1.0" encoding="UTF-8"?>
	<selector xmlns:android="http://schemas.android.com/apk/res/android">
	    <item android:drawable="@mipmap/home_normal" android:state_enabled="true" />
	    <item android:drawable="@mipmap/home_disabled" android:state_enabled="false" />
	</selector>


    <?xml version="1.0" encoding="utf-8"?>
	<shape xmlns:android="http://schemas.android.com/apk/res/android"
	    android:shape="rectangle">
	    <stroke
	        android:width="2dp"
	        android:color="#F0F0F0" />
	    <!-- 矩形的圆角半径 -->
	    <corners android:radius="12dp" />
	</shape>

## 4.5 切换fragment ###

    
    private void changeFragment(int indexOfChild) {
        //需要切换到哪个fragment显示
        Fragment fragment = fragmentArrayList.get(indexOfChild);
        //替换帧布局内部内容
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, fragment).commit();
    }
## 5 商家列表模块 ##
>商家列表模块,采用RecyclerView展示列表数据
### 5.1 请求网络url地址和参数的指定 ###
>不管是Fragment还是activity都需要请求网络,所以将网络请求获取数据的操作封装在BasePresenter中统一处理
>项目中采用Retrofit进行网络请求

	导入包 :

    implementation 'com.squareup.retrofit2:retrofit:2.1.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.1.0'

	public abstract class BasePresenter {
	    //在BasePresenter的子类中,需要发送不同的网络请求,所以将其定义成共有,供子类使用
	    private static final String TAG = "BasePresenter";
	    public ResponseInfoApi responseInfoApi;
	    private HashMap<String, String> errorMap = new HashMap<>();
	
	    public BasePresenter() {
	        //retrofit请求网络,jar包
	        //建立一个错误状态码额错误内容的索引表
	        errorMap.put("1", "数据没有更新");
	        errorMap.put("2", "请求链接地址无效");
	        errorMap.put("3", "请求参数异常");
	        //创建retorfit对象
	        Retrofit retrofit = new Retrofit.Builder()
	                //约定需要解析的数据格式
	                .addConverterFactory(GsonConverterFactory.create())
	                .baseUrl(Constant.BASEURL)
	                .build();
	        //ResponseInfoApi约定每一个请求应该如何发送
	        responseInfoApi = retrofit.create(ResponseInfoApi.class);
	        //请求发送出去,获取首页的数据
	//        Call<ResponseInfo> homeInfo = responseInfoApi.getHomeInfo();
	        //获取首页数据成功或失败的回到方法
	//      homeInfo.enqueue(new Callback<ResponseInfo>() {
	////          @Override
	////          public void onResponse(Call<ResponseInfo> call, Response<ResponseInfo> response) {
	////
	////          }
	////
	////          @Override
	////          public void onFailure(Call<ResponseInfo> call, Throwable t) {
	////
	////          }
	////      });
	    }
	
	    public class CallBackAdapter implements Callback<ResponseInfo> {
	
	        @Override
	        public void onResponse(Call<ResponseInfo> call, Response<ResponseInfo> response) {
	            Log.e(TAG, "onResponse: 1" + (response == null));
	            ResponseInfo responseInfo = response.body();
	            Log.e(TAG, "onResponse: 2" + (responseInfo == null));
	            if (responseInfo != null && !responseInfo.equals("")) {
	                String code = responseInfo.getCode();
	                if (code != null || !code.equals("")) {
	                    if (code.equals("0")) {
	                        //有意义,则需要进行解析展示
	                        parseJson(responseInfo.getData());
	                    } else {
	                        String errorMessage = errorMap.get(code);
	                        //如何处理错误
	                        onFailure(call, new RuntimeException(errorMessage));
	                    }
	                }
	            }
	        }
	
	        @Override
	        public void onFailure(Call<ResponseInfo> call, Throwable t) {
	            if (t instanceof RuntimeException) {
	                //请求服务器成功,状态码不为0的问题
	                showErrorMessage(t.getMessage());
	            }
	            //还是自身代码问题(少权限)
	            showErrorMessage("服务器忙,请稍后重试");
	        }
	    }
	
	    /**
	     * @param message 具体错误的原因,此方法交由子类处理
	     */
	    protected abstract void showErrorMessage(String message);
	
	    /**
	     * @param data 需要解析的json,此json因为结构不同,所以交由子类实现
	     */
	    protected abstract void parseJson(String data);
	}
	
	public interface ResponseInfoApi {
	    //维护好每一个网络请求应该如何发送
	    //请求方式
	    //请求地址
	    //请求参数
	    //请求结果
	    @GET(Constant.HOME)
	    Call<ResponseInfo> getHomeInfo(@Query("latitude") String lat, @Query("longitude") String lng);
	
	    @GET(Constant.HOME)
	    Call<ResponseInfo> getHomeInfo();
	
	    //商品列表页也需要数据
	    @GET(Constant.BUSINESS)
	    Call<ResponseInfo> getGoodsInfo(@Query("sellerId") long sellerId);
	
	    @GET(Constant.LOGIN)
	    Call<ResponseInfo> getLoginInfo(@Query("username") String username, @Query("password") String password, @Query("phone") String phone, @Query("type") int type);
	
	    //订单模块数据请求方法
	    @GET(Constant.ORDER)
	    Call<ResponseInfo> getOrderInfo(@Query("userId") int userId);
	}
### 5.2 请求网络具体流程 ###
	在BasePresenter中定义请求网络成功、失败的回调方法
	

    public class CallBackAdapter implements Callback<ResponseInfo> {

        @Override
        public void onResponse(Call<ResponseInfo> call, Response<ResponseInfo> response) {
            Log.e(TAG, "onResponse: 1" + (response == null));
            ResponseInfo responseInfo = response.body();
            Log.e(TAG, "onResponse: 2" + (responseInfo == null));
            if (responseInfo != null && !responseInfo.equals("")) {
                String code = responseInfo.getCode();
                if (code != null || !code.equals("")) {
                    if (code.equals("0")) {
                        //有意义,则需要进行解析展示
                        parseJson(responseInfo.getData());
                    } else {
                        String errorMessage = errorMap.get(code);
                        //如何处理错误
                        onFailure(call, new RuntimeException(errorMessage));
                    }
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseInfo> call, Throwable t) {
            if (t instanceof RuntimeException) {
                //请求服务器成功,状态码不为0的问题
                showErrorMessage(t.getMessage());
            }
            //还是自身代码问题(少权限)
            showErrorMessage("服务器忙,请稍后重试");
        }
    }

    /**
     * @param message 具体错误的原因,此方法交由子类处理
     */
    protected abstract void showErrorMessage(String message);

    /**
     * @param data 需要解析的json,此json因为结构不同,所以交由子类实现
     */
    protected abstract void parseJson(String data);

### 5.3 根据网络请求返回的json结果,编写相应的javabean ###

    public class ResponseInfo {
	    private String code;
	    private String data;
	
	    public String getCode() {
	        return code;
	    }
	
	    public void setCode(String code) {
	        this.code = code;
	    }
	
	    public String getData() {
	        return data;
	    }
	
	    public void setData(String data) {
	        this.data = data;
	    }
	}
### 5.4 网络请求的触发 ###

    
    /**
     * 请求网络的触发方法
     */
    public void getHomeData() {
        Log.e(TAG, "getHomeData: 333");
        Call<ResponseInfo> homeInfo = responseInfoApi.getHomeInfo();
        homeInfo.enqueue(new CallBackAdapter());
    }

### 5.5 BasePresenter使用 ###
>创建BasePresenter的子类HomePresenter,详细处理homeFragment的网络请求

	`public class HomePresenter extends BasePresenter {
	    private static final String TAG = "HomePresenter";
	    private HomeAdapter homeAdapter;
	
	    public HomePresenter(HomeAdapter homeAdapter) {
	        this.homeAdapter = homeAdapter;
	    }
	
	    @Override
	    protected void showErrorMessage(String message) {
	
	    }
	
	    @Override
	    protected void parseJson(String data) {
	        Log.i(TAG, "showErrorMessage: " + data);
	        Gson gson = new Gson();
	        HomeInfo homeInfo = gson.fromJson(data, HomeInfo.class);
	        //数据需要放置在数据适配器中列表展示???
	        homeAdapter.setData(homeInfo);
	    }
	
	    /**
	     * 请求网络的触发方法
	     */
	    public void getHomeData() {
	        Log.e(TAG, "getHomeData: 333");
	        Call<ResponseInfo> homeInfo = responseInfoApi.getHomeInfo();
	        homeInfo.enqueue(new CallBackAdapter());
	    }
	}`
## 6 首页数据展示 ##
### 6.1 首页HomeFragment指定布局,添加界面  ###
>首页的HomeFragment中用RecyclerView展示列表数据,首页搭建

    public class HomeFragment extends BaseFragment {
	    private static final String TAG = "HomeFragment";
	    @BindView(R.id.rv_home)
	    RecyclerView rvHome;
	    @BindView(R.id.home_tv_address)
	    TextView homeTvAddress;
	    @BindView(R.id.ll_title_search)
	    LinearLayout llTitleSearch;
	    @BindView(R.id.ll_title_container)
	    LinearLayout llTitleContainer;
	    private Unbinder unbinder;
	    //argbEvaluator有相关的方法可以管理颜色的渐变
	    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();
	    int sumY = 0;
	
	    @Override
	    public void onCreate(@Nullable Bundle savedInstanceState) {
	        sumY = 0;
	        super.onCreate(savedInstanceState);
	    }
	
	    @Nullable
	    @Override
	    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
	        View view = View.inflate(getActivity(), R.layout.fragment_home, null);
	        unbinder = ButterKnife.bind(this, view);
	        return view;
	    }
	
	    @Override
	    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
	        HomeAdapter homeAdapter = new HomeAdapter(getActivity());
	        rvHome.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
	        rvHome.setAdapter(homeAdapter);
	        HomePresenter homePresenter = new HomePresenter(homeAdapter);
	        Log.e(TAG, "onActivityCreated: 222");
	        homePresenter.getHomeData();
	        rvHome.addOnScrollListener(new RecyclerView.OnScrollListener() {
	            @Override
	            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
	                //滚动过程中发生变化
	                super.onScrollStateChanged(recyclerView, newState);
	            }
	
	            @Override
	            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
	                //滚动中,需要将每一次滚动的距离进行累加,累加结果存储在sumY中
	                sumY += dy;
	                //修改此控件中的背景颜色
	                int bgColor = 0X553190E8;
	                if (sumY == 0) {
	                    //没有移动时,色值就是初始色值
	                    bgColor = 0X553190E8;
	                } else if (sumY >= 300) {
	                    //如果移动达到300个像素,则色值和状态栏一样的色值
	                    bgColor = 0XFF3190E8;
	                } else {
	                    //0---300随着移动距离的增加,颜色进行变化
	                    bgColor = (int) argbEvaluator.evaluate(sumY / 300.0f, 0X553190E8, 0XFF3190E8);
	                }
	                //将管理好的色值设置给顶部title作为背景
	                llTitleContainer.setBackgroundColor(bgColor);
	                super.onScrolled(recyclerView, dx, dy);
	            }
	        });
	        super.onActivityCreated(savedInstanceState);
	    }
	
	    @Override
	    public void onDestroyView() {
	        super.onDestroyView();
	        unbinder.unbind();
	    }
	}
### 6.2 首页RecyclerView数据适配器实现
	首页中因为需要显示轮播图效果,所以需要使用相关jar包
	//顶部轮播图需要引入jar包  .jar @aar包含资源文件（布局，图片）

    //异步加载图片
    implementation 'com.squareup.picasso:picasso:2.3.2'
    //兼容低版本动画jar包
    implementation 'com.nineoldandroids:library:2.4.0'
    //SliderLayout自定义控件所在jar包
    implementation 'com.daimajia.slider:library:1.1.5@aar'
	
    public class HomeAdapter extends RecyclerView.Adapter {
	    public static final int ITEM_HEADER = 0; //索引为0的时候,头条目状态码
	    public static final int ITEM_SELLER = 1; //商家条目状态码
	    public static final int ITEM_DIV = 2;//分分割线条目状态码
	    private Activity activity;
	    private HomeInfo homeInfo;
	
	    public HomeAdapter(Activity activity) {
	        this.activity = activity;
	    }
	
	    @NonNull
	    @Override
	    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
	        //不同的条目布局效果不一样,则会有不同的viewholder对象存储控件
	        if (viewType == ITEM_HEADER) {
	            //1.将布局转换成view对象
	            View view = View.inflate(activity, R.layout.item_title, null);
	            //2.创建viewholder对象
	            HeaderViewHolder headerViewHolder = new HeaderViewHolder(view);
	            return headerViewHolder;
	        } else if (viewType == ITEM_DIV) {
	            View view = View.inflate(activity, R.layout.item_division, null);
	            DivViewHolder divViewHolder = new DivViewHolder(view);
	            return divViewHolder;
	        } else {
	            //3.商家viewholder对象创建
	            View view = View.inflate(activity, R.layout.item_seller, null);
	            SellerViewHolder sellerViewHolder = new SellerViewHolder(view);
	            return sellerViewHolder;
	        }
	    }
	
	    @Override
	    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
	        if (holder instanceof HeaderViewHolder) {
	            //因为头轮播图的数据,在HeaderViewHolder构造方法中已经设置过了,所以在此处无代码
	        } else if (holder instanceof DivViewHolder) {
	            //设置分割线的数据
	            setDivViewHolderData(holder, position - 1);
	        } else {
	            //设置普通商家条目的数据
	            setSellerViewHolderData(holder, position - 1);
	            ((SellerViewHolder) holder).setPosition(position - 1);
	        }
	    }
	
	    public void setSellerViewHolderData(RecyclerView.ViewHolder holder, int position) {
	        Seller seller = homeInfo.getBody().get(position).getSeller();
	        //设置商家名称
	        ((SellerViewHolder) holder).tvTitle.setText(seller.getName());
	        //评分,星星个数
	        if (!TextUtils.isEmpty(seller.getScore())) {
	            float score = Float.parseFloat(seller.getScore());
	            ((SellerViewHolder) holder).ratingBar.setRating(score);
	        }
	    }
	
	    public void setDivViewHolderData(RecyclerView.ViewHolder holder, int position) {
	        List<String> recommendInfos = homeInfo.getBody().get(position).getRecommendInfos();
	        ((DivViewHolder) holder).tv1.setText(recommendInfos.get(0));
	        ((DivViewHolder) holder).tv2.setText(recommendInfos.get(1));
	        ((DivViewHolder) holder).tv3.setText(recommendInfos.get(2));
	        ((DivViewHolder) holder).tv4.setText(recommendInfos.get(3));
	        ((DivViewHolder) holder).tv5.setText(recommendInfos.get(4));
	        ((DivViewHolder) holder).tv6.setText(recommendInfos.get(5));
	    }
	
	    /**
	     * 3中条目类型展示
	     *
	     * @param position 列表索引值
	     * @return 根据列表的索引值判断此索引位置需要显示的条目类型是那种(头, 分割线, 商家)
	     */
	    @Override
	    public int getItemViewType(int position) {
	        if (position == 0) {
	            //返回头的条目类型状态码
	            return ITEM_HEADER;
	        } else if (homeInfo.getBody().get(position - 1).getType() == 1) {
	            //返回分割线条目状态码
	            return ITEM_DIV;
	        } else {
	            //返回商家条目状态码
	            return ITEM_SELLER;
	        }
	    }
	
	    @Override
	    public int getItemCount() {
	        if (homeInfo != null && homeInfo.getBody() != null && homeInfo.getBody().size() > 0) {
	            //当前列表的条目数据 = 商品列表个数+1(顶部轮播图头条目)
	            return homeInfo.getBody().size() + 1;
	        }
	        return 0;
	    }
	
	    /**
	     * @param homeInfo 首页请求网络获取的数据
	     *                 head object 用户填充包含轮播图的头
	     *                 body list 用户填充商铺列表
	     */
	    public void setData(HomeInfo homeInfo) {
	        this.homeInfo = homeInfo;
	        //在获取数据后,需要告知数据适配器按照传递的数据进行刷新
	        notifyDataSetChanged();
	    }
	
	    class HeaderViewHolder extends RecyclerView.ViewHolder implements BaseSliderView.OnSliderClickListener {
	        @BindView(R.id.slider)
	        SliderLayout slider;
	
	        public HeaderViewHolder(View itemView) {
	            super(itemView);
	            ButterKnife.bind(this, itemView);
	            //结婚中包含了3个对象,3个对象中既有图片描述,图片的链接地址
	            ArrayList<Promotion> promotionList = homeInfo.getHead().getPromotionList();
	            for (int i = 0; i < promotionList.size(); i++) {
	                TextSliderView textSliderView = new TextSliderView(activity);
	                textSliderView.description(promotionList.get(i).getInfo())//描述文本
	                        .image(promotionList.get(i).getPic())//展示图片内容
	                        .setScaleType(BaseSliderView.ScaleType.Fit)//前景填充背景方式
	                        .setOnSliderClickListener(this);
	                //将包含了图片和描述了文本内容的控件添加到容器内
	                slider.addSlider(textSliderView);
	            }
	            //SliderLayout指定切换内部图片的动画效果
	            slider.setPresetTransformer(SliderLayout.Transformer.Tablet);
	            //指示器的所在位置
	            slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
	            //描述内容出现的动画
	            slider.setCustomAnimation(new DescriptionAnimation());
	            //切换页面间隔时长
	            slider.setDuration(4000);
	        }
	
	        @Override
	        public void onSliderClick(BaseSliderView slider) {
	
	        }
	    }
	
	    class DivViewHolder extends RecyclerView.ViewHolder {
	        @BindView(R.id.tv_division_title)
	        TextView tvDivisionTitle;
	        @BindView(R.id.tv1)
	        TextView tv1;
	        @BindView(R.id.tv2)
	        TextView tv2;
	        @BindView(R.id.tv3)
	        TextView tv3;
	        @BindView(R.id.tv4)
	        TextView tv4;
	        @BindView(R.id.tv5)
	        TextView tv5;
	        @BindView(R.id.tv6)
	        TextView tv6;
	
	        public DivViewHolder(View itemView) {
	            super(itemView);
	            ButterKnife.bind(this, itemView);
	        }
	    }
	
	    class SellerViewHolder extends RecyclerView.ViewHolder {
	        @BindView(R.id.tvCount)
	        TextView tvCount;
	        @BindView(R.id.tv_title)
	        TextView tvTitle;
	        @BindView(R.id.ratingBar)
	        RatingBar ratingBar;
	        private int position;
	
	        public SellerViewHolder(View itemView) {
	            super(itemView);
	            ButterKnife.bind(this, itemView);
	            itemView.setOnClickListener(new View.OnClickListener() {
	                @Override
	                public void onClick(View v) {
	                    //position点中条目索引位置
	                    //通过索引从服务器返回的商品列表集合中,找到指定的商家,让后一个页面加载此商家的数据
	                    Seller seller = homeInfo.getBody().get(position).getSeller();
	                    //此seller会作为区分不同商家对象
	                    Intent intent = new Intent(activity, BusinessActivity.class);
	                    intent.putExtra("seller", seller);
	                    activity.startActivity(intent);
	                }
	            });
	        }
	
	        public void setPosition(int position) {
	            this.position = position;
	        }
	    }
	}
## 7 首页滚动RecyclerView达到Title渐变效果 ##
### 7.1 创建的Activity继承至FragmentActivity则默认不显示Title ###

            rvHome.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
	            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
	                //滚动过程中发生变化
	                super.onScrollStateChanged(recyclerView, newState);
	            }
	
	            @Override
	            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
	                //滚动中,需要将每一次滚动的距离进行累加,累加结果存储在sumY中
	                sumY += dy;
	                //修改此控件中的背景颜色
	                int bgColor = 0X553190E8;
	                if (sumY == 0) {
	                    //没有移动时,色值就是初始色值
	                    bgColor = 0X553190E8;
	                } else if (sumY >= 300) {
	                    //如果移动达到300个像素,则色值和状态栏一样的色值
	                    bgColor = 0XFF3190E8;
	                } else {
	                    //0---300随着移动距离的增加,颜色进行变化
	                    bgColor = (int) argbEvaluator.evaluate(sumY / 300.0f, 0X553190E8, 0XFF3190E8);
	                }
	                //将管理好的色值设置给顶部title作为背景
	                llTitleContainer.setBackgroundColor(bgColor);
	                super.onScrolled(recyclerView, dx, dy);
	            }
	        });

## 8 商品界面 ## 
	点击一般类型条目，跳转至商品详情界面
	

        public SellerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //position点中条目索引位置
                    //通过索引从服务器返回的商品列表集合中,找到指定的商家,让后一个页面加载此商家的数据
                    Seller seller = homeInfo.getBody().get(position).getSeller();
                    //此seller会作为区分不同商家对象
                    Intent intent = new Intent(activity, BusinessActivity.class);
                    intent.putExtra("seller", seller);
                    activity.startActivity(intent);
                }
            });
        }
### 8.1 商品界面viewpager+tablayout关联 ###
>详情界面底部需要弹出悬浮框,所以需要使用BottomSheetLayout自定义控件,如下是导入jar包方式
>
    //底部弹出窗体
    implementation 'com.flipboard:bottomsheet-commons:1.5.1'
    implementation 'com.flipboard:bottomsheet-core:1.5.1'
	
	public class BusinessActivity extends FragmentActivity {
	    @BindView(R.id.ib_back)
	    ImageButton ibBack;
	    @BindView(R.id.tv_title)
	    TextView tvTitle;
	    @BindView(R.id.ib_menu)
	    ImageButton ibMenu;
	    @BindView(R.id.tabs)
	    TabLayout tabs;
	    @BindView(R.id.vp)
	    ViewPager vp;
	    @BindView(R.id.bottomSheetLayout)
	    BottomSheetLayout bottomSheetLayout;
	    @BindView(R.id.imgCart)
	    ImageView imgCart;
	    @BindView(R.id.tvSelectNum)
	    TextView tvSelectNum;
	    @BindView(R.id.tvCountPrice)
	    TextView tvCountPrice;
	    @BindView(R.id.tvDeliveryFee)
	    TextView tvDeliveryFee;
	    @BindView(R.id.tvSendPrice)
	    TextView tvSendPrice;
	    @BindView(R.id.tvSubmit)
	    TextView tvSubmit;
	    @BindView(R.id.bottom)
	    LinearLayout bottom;
	    @BindView(R.id.fl_Container)
	    FrameLayout flContainer;
	    private Seller seller;
	    public BusinessPresenter businessPresenter;
	    private float floatDeliveryFee;
	    private float floatSendPrivice;
	    private View sheetView;
	    public MyFragmentPagerAdapter myFragmentPagerAdapter;
	    private ShopCartAdapter shopCartAdapter;
	
	    @Override
	    protected void onCreate(@Nullable Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_bussiness);
	        ButterKnife.bind(this);
	//        bottomSheetLayout.dismissSheet();//小时弹出对话框
	//        bottomSheetLayout.showWithSheetView(view);//弹出对话框,并且制定显示view对象
	//        bottomSheetLayout.isSheetShowing();//判断对话框处于显示还是隐藏状态
	        businessPresenter = new BusinessPresenter(this);
	        //接收前一个界面传送过来的数据
	        seller = (Seller) getIntent().getSerializableExtra("seller");
	        initTab();
	        floatDeliveryFee = Float.parseFloat(seller.getDeliveryFee());
	        String strDeliverFee = CountPriceFormater.format(floatDeliveryFee);
	        //配送费
	        tvDeliveryFee.setText("配送费:" + strDeliverFee);
	        floatSendPrivice = Float.parseFloat(seller.getSendPrice());
	        String strSendPrice = CountPriceFormater.format(floatSendPrivice);
	        //起送价
	        tvSendPrice.setText("起送:" + strSendPrice);
	    }
	
	    private void initTab() {
	        //1.给viewPager设置数据适配器,FragemntPagerAdapter
	        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), seller);
	        vp.setAdapter(myFragmentPagerAdapter);
	        //2.tablayout和设置完数据适配器的viewpager绑定
	        tabs.setupWithViewPager(vp);
	    }
	
	    /**
	     * @param imageview 需要添加到imageview
	     * @param width     添加的imageview宽度
	     * @param height    添加的imageview高度
	     */
	    public void addFlyImageView(ImageView imageview, int width, int height) {
	        flContainer.addView(imageview, width, height);
	    }
	
	    /**
	     * @return 获取购物车所在屏幕的位置
	     */
	    public int[] getShopCartLocation() {
	        //1.获取购物车的控件
	        int[] shopcartLocation = new int[2];
	        imgCart.getLocationInWindow(shopcartLocation);
	        return shopcartLocation;
	    }
	
	    /**
	     * @param totalCount  购物车需要显示的的总数量
	     * @param totalPrices 购物车需要显示的总金额
	     */
	    public void refreshShopCartData(int totalCount, float totalPrices) {
	        //如果totalCount的值大于0,则需要显示气泡和购物车中总金额
	        if (totalCount > 0) {
	            tvSelectNum.setVisibility(View.VISIBLE);
	            tvSelectNum.setText(totalCount + "");
	            String strTotalPrice = CountPriceFormater.format(totalPrices);
	            tvCountPrice.setText(strTotalPrice);
	        } else {
	            tvSelectNum.setVisibility(View.GONE);
	            tvCountPrice.setText(CountPriceFormater.format(0.0f));
	        }
	        //判断去结算按钮是否出现,起送金额是否出现
	        if (totalPrices > floatSendPrivice) {
	            //购买金额>起送价,显示区结算按钮
	            tvSubmit.setVisibility(View.VISIBLE);
	            tvSendPrice.setVisibility(View.GONE);
	        } else {
	            //购买金额<起送价,显示起送价,提示用户还没买够
	            tvSubmit.setVisibility(View.GONE);
	            tvSendPrice.setVisibility(View.VISIBLE);
	        }
	    }
	
	    @OnClick({R.id.tvSubmit, R.id.bottom})
	    public void onViewClicked(View view) {
	        switch (view.getId()) {
	            case R.id.tvSubmit:
	                Intent intent = new Intent(this, ConfirmOrderActivity.class);
	                ArrayList<GoodsInfo> shopCartList1 = (ArrayList<GoodsInfo>) businessPresenter.getShopCartList();
	                //购买商品的集合
	                intent.putExtra("shopcartList", shopCartList1);
	                //运费数量
	                intent.putExtra("deliverFee", floatDeliveryFee);
	                startActivity(intent);
	                break;
	            case R.id.bottom:
	                if (sheetView == null) {
	                    //将布局文件转换成sheetView供使用
	                    sheetView = onCreateView();
	                }
	                //判断弹出还是隐藏对话框
	                if (bottomSheetLayout.isSheetShowing()) {
	                    bottomSheetLayout.dismissSheet();
	                } else {
	                    //重新获取加入购物车的数据集合
	                    List<GoodsInfo> shopCartList = businessPresenter.getShopCartList();
	                    if (shopCartList != null && shopCartList.size() > 0) {
	                        bottomSheetLayout.showWithSheetView(sheetView);
	                        //将刚刚获取的数据,更新至数据适配器中
	                        shopCartAdapter.setData(shopCartList);
	                    }
	                }
	                break;
	        }
	    }
	
	    private View onCreateView() {
	        View view = View.inflate(this, R.layout.cart_list, null);
	        TextView tvClear = view.findViewById(R.id.tvClear);
	        RecyclerView rvCart = view.findViewById(R.id.rvCart);
	        tvClear.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                showDialog();
	            }
	        });
	        shopCartAdapter = new ShopCartAdapter(this);
	        rvCart.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
	        rvCart.setAdapter(shopCartAdapter);
	        List<GoodsInfo> shopCartList = businessPresenter.getShopCartList();
	        shopCartAdapter.setData(shopCartList);
	        return view;
	    }
	
	    private void showDialog() {
	        AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setMessage("是否清空?");
	        builder.setPositiveButton("清空", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                dialog.dismiss();
	                businessPresenter.clearAll();
	            }
	        });
	        builder.setNegativeButton("稍后", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                dialog.dismiss();
	            }
	        });
	        builder.show();
	    }
	
	    /**
	     * 隐藏BusinessActivity对话框
	     */
	    public void hiddenDialog() {
	        if (bottomSheetLayout.isSheetShowing()) {
	            bottomSheetLayout.dismissSheet();
	        }
	    }
	}

	public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
	    private Seller seller;
	    private String[] title;
	    private List<Fragment> fragmentList = new ArrayList<>();
	    public MyFragmentPagerAdapter(FragmentManager fm,Seller seller) {
	        super(fm);
	        this.seller = seller;
	        title = new String[]{"商品","评价","商家"};
	    }
	
	    @Override
	    public Fragment getItem(int position) {
	        //根据索引返回不同的fragemnt对象,现在选项卡有3个,fragment界面就有3个
	        BaseFragment baseFragment = null;
	        switch (position){
	            case 0:
	                baseFragment = new GoodsFragment();
	                break;
	            case 1:
	                baseFragment = new SuggestFragment();
	                break;
	            case 2 :
	                baseFragment = new SellerFragment();
	                break;
	        }
	        //1.seller需要存储在bundle进行传递
	        Bundle bundle = new Bundle();
	        //2.将seller存储在bundle中
	        bundle.putSerializable("seller",seller);
	        //3.把邮包传递给每一个fragment
	        baseFragment.setArguments(bundle);
	        if (!fragmentList.contains(baseFragment)){
	            fragmentList.add(baseFragment);
	        }
	        return baseFragment;
	    }
	    //获取GoodsFragment方法
	    public GoodsFragment getGoodsFragment(){
	        if (fragmentList!=null&&fragmentList.size()>0){
	            return (GoodsFragment) fragmentList.get(0);
	        }
	        return null;
	    }
	
	    /**
	     * @return
	     */
	    @Override
	    public int getCount() {
	        return title.length;
	        //告知viewpager一共几个fragment界面
	    }
	
	    /**
	     * @param position
	     * @return
	     * 给viewpager绑定tab设置标题文本内容方法
	     */
	    @Nullable
	    @Override
	    public CharSequence getPageTitle(int position) {
	        return title[position];
	    }
	}


# 用户登录 #
## 短信验证步骤 ##
>用户登录模块采用短信验证的方式,登录用户,所以采用sharesdk进行短信验证
>	
	
	查看sharesdk文档需要做如下操作
	
	步骤一:下载sharesdk短信验证相关jar包和demo,将jar包中aar文件拷贝至工程libs目录下
			SMSSDK-<version>.aar          SMSSDK 核心         必须
			SMSSDKGUI-<version>.aar       SMSSDK GUI 开源库   可选

		  添加如下代码,让上诉2个aar文件可以在工程中使用
			repositories {
			    flatDir {
			        dirs 'libs' //就是你放aar的目录地址
			    }
			}

	    implementation name: 'SMSSDK-2.1.3', ext: 'aar'
	    implementation name: 'SMSSDKGUI-2.1.3', ext: 'aar'
	步骤二:导入jar包,将jar包添加至工作目录
	    implementation files('libs/MobCommons-2016.1201.1839.jar')
	    implementation files('libs/MobTools-2016.1201.1839.jar')
	步骤三:添加sharesdk需要用到权限
		<uses-permission android:name="android.permission.READ_CONTACTS" />
	    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
	    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
	    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
	    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
	    <uses-permission android:name="android.permission.INTERNET" />
	    <uses-permission android:name="android.permission.RECEIVE_SMS" />
	    <uses-permission android:name="android.permission.READ_SMS" />
	    <uses-permission android:name="android.permission.GET_TASKS" />
	    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
	步骤四:权限校验: SMSUtil.checkPermission(this);
	步骤五:初始化工具: SMSSDK.initSDK(this, "262b6c80ec6e0", "be77483ca25cb86b248e9d564f2988ee");
	步骤六:注册事件监听：        //通过注册EventHandler判断验证码下发成功还是失败
        SMSSDK.registerEventHandler(eventHandler);
	步骤七:获取验证码：//给指定手机下发验证码短信
            SMSSDK.getVerificationCode("86", phone, new OnSendMessageHandler() {
                @Override
                public boolean onSendMessage(String s, String s1) {
                    return false;
                }
            });
	步骤八:发送验证码：            //短信验证码的校验
            SMSSDK.submitVerificationCode("86",phone,code);
	步骤九:注销监听:

	    @Override
	    protected void onDestroy() {
	        super.onDestroy();
	        SMSSDK.unregisterEventHandler(eventHandler);
	    }
		
## 1 短信注册获取验证码 ##

### 1.1 配置初始化信息 ###
	
        //通过注册EventHandler判断验证码下发成功还是失败
        SMSSDK.registerEventHandler(eventHandler);
### 1.2 注册按钮点击事件 ###
	

    @OnClick({R.id.tv_user_code, R.id.login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_user_code:
                sendCode();
                break;
            case R.id.login:
                checkCode();
                break;
        }
    }


	
    private void sendCode() {
        //判断是否有输入电话号码,电话号码合法性
        String phone = etUserPhone.getText().toString();
        if (!TextUtils.isEmpty(phone)){
            //给指定手机下发验证码短信
            SMSSDK.getVerificationCode("86", phone, new OnSendMessageHandler() {
                @Override
                public boolean onSendMessage(String s, String s1) {
                    return false;
                }
            });
            tvUserCode.setEnabled(false);
            new Thread(){
                @Override
                public void run() {
                    while (time>0){
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessage(KEEP_TIME_DESC);
                    }
                    handler.sendEmptyMessage(RESET_TIME);
                    super.run();
                }
            }.start();
        }else{
            Toast.makeText(this, "手机号码为空", Toast.LENGTH_SHORT).show();
        }
    }
	
### 1.3 监听验证码是否发送成功

    private EventHandler eventHandler = new EventHandler() {
        @Override
        public void afterEvent(int i, int i1, Object o) {
            if (i == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                //判断当前以及触发的事件,是不是下发验证码短信
                if (i1 == SMSSDK.RESULT_COMPLETE) {
                    Log.e(TAG, "afterEvent: 短信下发成功");
                } else {
                    Log.e(TAG, "afterEvent: 短信下发失败");
                }
            } else {
                //校验验证码
                if (i1 == SMSSDK.RESULT_COMPLETE) {
                    Log.e(TAG, "afterEvent: 校验验证码成功");
                } else {
                    Log.e(TAG, "afterEvent: 校验验证码失败");
                    //子线程,如果要通过吐司提示用户,则需要通过发消息给handlerMessage方法,然后在操作UI
                }
            }
            super.afterEvent(i, i1, o);
        }
    };


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case KEEP_TIME_DESC:
                    //时间递减1s
                    time--;
                    tvUserCode.setText("稍后再发("+time+")");
                    break;
                case RESET_TIME:
                    //当倒计时结束,则让空间在此可用
                    tvUserCode.setEnabled(true);
                    tvUserCode.setText("重新发送");
                    time = 60;
                    break;
            }
        }
    };
### 1.4 验证码是否匹配
>给指定手机发送的验证码和提交的验证码是否一致,如果一致,则说明可以开始注册了


    private void checkCode() {
        String phone = etUserPhone.getText().toString();
        String psd = etUserPsd.getText().toString();
        String code = etUserCode.getText().toString();
        if (!TextUtils.isEmpty(phone)&&!TextUtils.isEmpty(psd)&&!TextUtils.isEmpty(code)){
            //短信验证码的校验
            SMSSDK.submitVerificationCode("86",phone,code);
            login();
        }
    }
## 2 用户注册 ##
>发送网络请求,注册用户一旦注册成功,需要将服务器的数据插入或者同步到本地的用户数据库
	

    //短信验证码校验成功后,才可以发送登录请求
    private void login() {
        String phone = etUserPhone.getText().toString().trim();
        String psd = etUserPsd.getText().toString().trim();
        if (!TextUtils.isEmpty(phone)&&!TextUtils.isEmpty(psd)){
            //发送网络请求,做登录
            LoginPresenter loginPresenter = new LoginPresenter(this);
            loginPresenter.getLoginData(phone,psd,phone,3);
        }
    }


    @GET(Constant.LOGIN)
    Call<ResponseInfo> getLoginInfo(@Query("username") String username, @Query("password") String password, @Query("phone") String phone, @Query("type") int type);

	public class LoginPresenter extends BasePresenter {
	    private Activity activity;
	
	    public LoginPresenter(Activity activity) {
	        this.activity = activity;
	    }
	
	    @Override
	    protected void showErrorMessage(String message) {
	
	    }
	
	    @Override
	    protected void parseJson(String data) {
	        Gson gson = new Gson();
	        //当前登录用户的信息在userinfo中
	        UserInfo userInfo = gson.fromJson(data, UserInfo.class);
	        MyApplication.userId = userInfo.get_id();
	        //获取dao对象
	        DBHelper dbHelper = new DBHelper(activity);
	        //泛型一 确认操作表是哪一张,userinfo指向的t_user表
	        //泛型二 userinfo唯一性主键类型
	        Dao<UserInfo, Integer> dao = dbHelper.getDao(UserInfo.class);
	        SQLiteDatabase db = dbHelper.getWritableDatabase();
	        AndroidDatabaseConnection connection = new AndroidDatabaseConnection(db, true);
	        //1.将数据库表中所有的isLogin字段都设置为0
	        try {
	            //设置失误的回滚点
	            Savepoint savePoint = connection.setSavePoint("start");
	            //让ormlite不要自己管理事务,有我们的代码管理事务
	            connection.setAutoCommit(false);
	            List<UserInfo> userInfoList = dao.queryForAll();
	            for (int i = 0; i < userInfoList.size(); i++) {
	                UserInfo info = userInfoList.get(i);
	                info.setLogin(0);
	                //数据有没有更新到数据库中
	                dao.update(info);
	            }
	            UserInfo userBean = dao.queryForId(userInfo.get_id());
	            if (userBean != null) {
	                //现在的呢牢固的是老用户,则将老用户已有的数据,0修改1即可
	                userBean.setLogin(1);
	                dao.update(userBean);
	            } else {
	                //如果现在登录到额是新用户,新用户在数据库中没有记录数据,则需要插入一条isLogin为1的数据
	                userInfo.setLogin(1);
	                dao.create(userInfo);
	            }
	            //提交代码,至回滚点
	            connection.commit(savePoint);
	            //登录完成后,用户需要看到部分登录信息,将当前的登录页面finish()
	            activity.finish();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	
	    //触发网络请求方法
	    public void getLoginData(String username, String psd, String phone, int type) {
	        Call<ResponseInfo> loginInfo = responseInfoApi.getLoginInfo(username, psd, phone, 3);
	        loginInfo.enqueue(new CallBackAdapter());
	    }
	}
### 2.1 数据库操作 ###
>数据库操作使用
>
>
>进行增删改查

    //使用如下jar包可以通过方法替换相关的sql语句编写
    implementation 'com.j256.ormlite:ormlite-android:5.0'

>创建项目数据库takeout.db，创建用户信息表
	
	public class DBHelper extends OrmLiteSqliteOpenHelper {
	    //hashmap用户存储多个dao对象
	    private HashMap<String, Dao> daoHashMap = new HashMap<>();
	
	    public DBHelper(Context context) {
	        super(context, "tackout.db", null, 1);
	    }
	
	    @Override
	    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
	        try {
	            //userinfo-->t_user表
	            TableUtils.createTable(connectionSource, UserInfo.class);
	            TableUtils.createTable(connectionSource, ReceiptAddressBean.class);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	
	    @Override
	    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
	
	    }
	
	    /**
	     * dao可以对表进行crud,一个dao只能操作一张表中的数据
	     *
	     * @param clazz 类字节码文件
	     * @return dao对象
	     */
	    public Dao getDao(Class clazz) {
	        Dao dao = daoHashMap.get(clazz.getSimpleName());
	        if (dao == null) {
	            try {
	                dao = super.getDao(clazz);
	                daoHashMap.put(clazz.getSimpleName(), dao);
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	        return dao;
	    }
	
	    @Override
	    public void close() {
	        //清空daoHashMap
	        for (String key : daoHashMap.keySet()) {
	            Dao dao = daoHashMap.get(key);
	            dao = null;
	        }
	        super.close();
	    }
	}
### 2.2  用户信息javabean ###
	@DatabaseTable(tableName = "t_user")
	public class UserInfo {
	    @DatabaseField(id = true)
	    private int _id;
	    @DatabaseField()
	    private float balance;
	    @DatabaseField()
	    private float discount;
	    @DatabaseField()
	    private int integral;
	    @DatabaseField()
	    private String name;
	    @DatabaseField()
	    private String phone;
	    @DatabaseField()
	    private int isLogin; //1 代表已经登陆     0 代表没有登陆
	
	    public float getBalance() {
	        return balance;
	    }
	
	    public void setBalance(float balance) {
	        this.balance = balance;
	    }
	
	    public float getDiscount() {
	        return discount;
	    }
	
	    public void setDiscount(float discount) {
	        this.discount = discount;
	    }
	
	    public int getIntegral() {
	        return integral;
	    }
	
	    public void setIntegral(int integral) {
	        this.integral = integral;
	    }
	
	    public String getName() {
	        return name;
	    }
	
	    public void setName(String name) {
	        this.name = name;
	    }
	
	    public String getPhone() {
	        return phone;
	    }
	
	    public void setPhone(String phone) {
	        this.phone = phone;
	    }
	
	    public int get_id() {
	        return _id;
	    }
	
	    public void set_id(int _id) {
	        this._id = _id;
	    }
	
	    public int getLogin() {
	        return isLogin;
	    }
	
	    public void setLogin(int isLogin) {
	        this.isLogin = isLogin;
	    }
	}
### 2.3 用户登陆与数据库相关逻辑 ###
	
    @Override
    protected void parseJson(String data) {
        Gson gson = new Gson();
        //当前登录用户的信息在userinfo中
        UserInfo userInfo = gson.fromJson(data, UserInfo.class);
        MyApplication.userId = userInfo.get_id();
        //获取dao对象
        DBHelper dbHelper = new DBHelper(activity);
        //泛型一 确认操作表是哪一张,userinfo指向的t_user表
        //泛型二 userinfo唯一性主键类型
        Dao<UserInfo, Integer> dao = dbHelper.getDao(UserInfo.class);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        AndroidDatabaseConnection connection = new AndroidDatabaseConnection(db, true);
        //1.将数据库表中所有的isLogin字段都设置为0
        try {
            //设置失误的回滚点
            Savepoint savePoint = connection.setSavePoint("start");
            //让ormlite不要自己管理事务,有我们的代码管理事务
            connection.setAutoCommit(false);
            List<UserInfo> userInfoList = dao.queryForAll();
            for (int i = 0; i < userInfoList.size(); i++) {
                UserInfo info = userInfoList.get(i);
                info.setLogin(0);
                //数据有没有更新到数据库中
                dao.update(info);
            }
            UserInfo userBean = dao.queryForId(userInfo.get_id());
            if (userBean != null) {
                //现在的呢牢固的是老用户,则将老用户已有的数据,0修改1即可
                userBean.setLogin(1);
                dao.update(userBean);
            } else {
                //如果现在登录到额是新用户,新用户在数据库中没有记录数据,则需要插入一条isLogin为1的数据
                userInfo.setLogin(1);
                dao.create(userInfo);
            }
            //提交代码,至回滚点
            connection.commit(savePoint);
            //登录完成后,用户需要看到部分登录信息,将当前的登录页面finish()
            activity.finish();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	
# 购物车模块 #
## 1 给+号和-号按钮添加点击事件 ##
	
    
        @OnClick({R.id.ib_add, R.id.ib_minus})
        public void onViewClick(View view) {
            switch (view.getId()) {
                case R.id.ib_add:
                    //点击完+号后,则控件不可用
                    view.setEnabled(false);
                    addGoods(view);
                    operation = Constant.ADD;
                    break;
                case R.id.ib_minus:
                    deleteGoods();
                    operation = Constant.DELETE;
                    break;
            }

## 2 购物车模块添加删除商品动画 ##
>情况一:商品列表添加第一件商品时,需要将减号的图片滚动出现,并且出现抛物线效果.
>清空二:商品列表添加第二件商品时,数字叠加,出现抛物线效果
 

        /**
         * @param view 添加一件商品
         */
        private void addGoods(View view) {
            //选中条目对象count的值为0的时候,才有如下动画
            if (data.get(position).getCount() == 0) {
                //旋转
                RotateAnimation rotateAnimation = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                //透明度变化
                AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 1.0f);
                //平移(x轴,正值-------->0)
                TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 2.0f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
                AnimationSet animationSet = new AnimationSet(false);
                animationSet.addAnimation(rotateAnimation);
                animationSet.addAnimation(alphaAnimation);
                animationSet.addAnimation(translateAnimation);
                animationSet.setDuration(300);
                ibMinus.startAnimation(animationSet);
                //减号和数量控件必须显示,才可以让动画效果可见
                ibMinus.setVisibility(View.VISIBLE);
                tvCount.setVisibility(View.VISIBLE);
            }
            //用户点击+号添加一件商品,需要刷新数据适配器更新商品数量
            int addCount = data.get(position).getCount() + 1;
            data.get(position).setCount(addCount);
            notifyDataSetChanged();
            //点击加号时抛物线动画效果
            addFlyImage(view);
        }


> 获取顶部状态栏的高度值

	//获取status_bar_height资源的ID
	        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
	        if (resourceId > 0) {
	            //根据资源ID获取响应的尺寸值
	            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
	        }

## 3 抛物线效果 ##
	
        private void addFlyImage(View view) {
            //1.用户飞行小球
            ImageView imageview = new ImageView(businessActivity);
            imageview.setBackgroundResource(R.mipmap.button_add);
            //2.制定飞行小球的开始位置
            int[] viewLocation = new int[2];
            view.getLocationInWindow(viewLocation);
            //3.让imageview和view的所在位置重合
            imageview.setX(viewLocation[0]);
            imageview.setY(viewLocation[1] - MyApplication.statusBarHeight);
            //4.并且将小球需要添加在帧布局的页面中
            ((BusinessActivity) businessActivity).addFlyImageView(imageview, view.getWidth(), view.getHeight());
            //5.获取飞行起点和终点的坐标
            int[] desLocation = ((BusinessActivity) businessActivity).getShopCartLocation();
            int[] sourceLocation = new int[2];
            imageview.getLocationInWindow(sourceLocation);
            //6.飞
            fly(imageview, view, sourceLocation, desLocation);
        }

## 4 商品列表删除最后一件商品时,需要将减号的图片滚动消失 ##
	
    
        /**
         * 减少一件商品
         */
        private void deleteGoods() {
            //选中条目对象count值为1的时候,才有如下动画
            if (data.get(position).getCount() == 1) {
                //旋转
                RotateAnimation rotateAnimation = new RotateAnimation(720, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                //透明度变化
                AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.5f);
                //平移(x轴  正值--->0)
                TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 2.0f,
                        Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
                AnimationSet animationSet = new AnimationSet(false);
                animationSet.addAnimation(rotateAnimation);
                animationSet.addAnimation(alphaAnimation);
                animationSet.addAnimation(translateAnimation);
                animationSet.setDuration(300);
                ibMinus.startAnimation(animationSet);
                //减号一出去的动画执行完成后,需要将括号和数量在此隐藏
                animationSet.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        tvCount.setVisibility(View.GONE);
                        ibMinus.setVisibility(View.GONE);
                        //数量归0操作
                        data.get(position).setCount(0);
                        //因为数量和减号控件已经被隐藏了(等同于刷新数据适配器效果
                        //所以数据适配器没必要刷新
                        businessActivity.businessPresenter.getShopCarttCountAndPrice();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            } else {
                //数量减少
                //用户点击+号添加意见商品后,需要刷新数据适配器更新商品数量
                int deleteCount = data.get(position).getCount() - 1;
                data.get(position).setCount(deleteCount);
                notifyDataSetChanged();
            }
        }

## 5 通知左侧的商品分类列表更新 ##
	
    
    public void refreshUI(int typeId, int operation) {
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getId() == typeId) {
                    //找到了需要修改分类对象
                    switch (operation) {
                        case Constant.ADD:
                            int addCount = data.get(i).getCount() + 1;
                            data.get(i).setCount(addCount);
                            break;
                        case Constant.DELETE:
                            if (data.get(i).getCount() > 0) {
                                int deleteCount = data.get(i).getCount() - 1;
                                data.get(i).setCount(deleteCount);
                            }
                            break;
                    }
                    break;
                }
            }
            //刷新分类数据适配器方法
            notifyDataSetChanged();
        }
    }

## 6 更新购物车商品总价和总数量 ##

    
    //提供一个计算购物车中商品总数量,总金额方法
    public void getShopCarttCountAndPrice() {
        int totalCount = 0;
        float totalPrices = 0.0f;
        //获取右侧的商品列表集合数据,判断每一个对象中的count的值是否大于0,如果大于0,则需要记录下来
        GoodsFragment goodsFragment = businessActivity.myFragmentPagerAdapter.getGoodsFragment();
        if (goodsFragment != null) {
            GoodsAdapter goodsAdapter = goodsFragment.goodsAdapter;
            //goodsAdapter数据取出来
            ArrayList<GoodsInfo> goodsInfoList = goodsAdapter.getData();
            for (int i = 0; i < goodsInfoList.size(); i++) {
                GoodsInfo goodsInfo = goodsInfoList.get(i);
                if (goodsInfo.getCount() > 0) {
                    //总数量累加
                    totalCount += goodsInfo.getCount();
                    //总金额累加
                    totalPrices += goodsInfo.getNewPrice() * goodsInfo.getCount();
                }
            }
            Log.e(TAG, "getShopCarttCountAndPrice: " + totalCount);
            Log.e(TAG, "getShopCarttCountAndPrice: " + totalPrices);
            businessActivity.refreshShopCartData(totalCount, totalPrices);
        }
    }

## 7 点击按钮弹出底部的列表 ##
> 底部购物车条目的点击事件
 
	
    
    @OnClick({R.id.tvSubmit, R.id.bottom})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvSubmit:
                Intent intent = new Intent(this, ConfirmOrderActivity.class);
                ArrayList<GoodsInfo> shopCartList1 = (ArrayList<GoodsInfo>) businessPresenter.getShopCartList();
                //购买商品的集合
                intent.putExtra("shopcartList", shopCartList1);
                //运费数量
                intent.putExtra("deliverFee", floatDeliveryFee);
                startActivity(intent);
                break;
            case R.id.bottom:
                if (sheetView == null) {
                    //将布局文件转换成sheetView供使用
                    sheetView = onCreateView();
                }
                //判断弹出还是隐藏对话框
                if (bottomSheetLayout.isSheetShowing()) {
                    bottomSheetLayout.dismissSheet();
                } else {
                    //重新获取加入购物车的数据集合
                    List<GoodsInfo> shopCartList = businessPresenter.getShopCartList();
                    if (shopCartList != null && shopCartList.size() > 0) {
                        bottomSheetLayout.showWithSheetView(sheetView);
                        //将刚刚获取的数据,更新至数据适配器中
                        shopCartAdapter.setData(shopCartList);
                    }
                }
                break;
        }
    }


>创建购物车对应的view对象

        private View onCreateView() {
	        View view = View.inflate(this, R.layout.cart_list, null);
	        TextView tvClear = view.findViewById(R.id.tvClear);
	        RecyclerView rvCart = view.findViewById(R.id.rvCart);
	        tvClear.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                showDialog();
	            }
	        });
	        shopCartAdapter = new ShopCartAdapter(this);
	        rvCart.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
	        rvCart.setAdapter(shopCartAdapter);
	        List<GoodsInfo> shopCartList = businessPresenter.getShopCartList();
	        shopCartAdapter.setData(shopCartList);
	        return view;
	    }

>BusinessActivityPresenter封装购物车相关业务

    
	/**
	 * 作者：南宫燚滨
	 * 描述:
	 * 邮箱：nangongyibin@gmail.com
	 * 时间: 2018/6/4 16:21
	 */
	public class BusinessPresenter extends BasePresenter {
	    private static final String TAG = "BusinessPresenter";
	    private BusinessActivity businessActivity;
	
	    public BusinessPresenter(BusinessActivity businessActivity) {
	        this.businessActivity = businessActivity;
	    }
	
	    @Override
	    protected void showErrorMessage(String message) {
	
	    }
	
	    @Override
	    protected void parseJson(String data) {
	
	    }
	
	    //提供一个计算购物车中商品总数量,总金额方法
	    public void getShopCarttCountAndPrice() {
	        int totalCount = 0;
	        float totalPrices = 0.0f;
	        //获取右侧的商品列表集合数据,判断每一个对象中的count的值是否大于0,如果大于0,则需要记录下来
	        GoodsFragment goodsFragment = businessActivity.myFragmentPagerAdapter.getGoodsFragment();
	        if (goodsFragment != null) {
	            GoodsAdapter goodsAdapter = goodsFragment.goodsAdapter;
	            //goodsAdapter数据取出来
	            ArrayList<GoodsInfo> goodsInfoList = goodsAdapter.getData();
	            for (int i = 0; i < goodsInfoList.size(); i++) {
	                GoodsInfo goodsInfo = goodsInfoList.get(i);
	                if (goodsInfo.getCount() > 0) {
	                    //总数量累加
	                    totalCount += goodsInfo.getCount();
	                    //总金额累加
	                    totalPrices += goodsInfo.getNewPrice() * goodsInfo.getCount();
	                }
	            }
	            Log.e(TAG, "getShopCarttCountAndPrice: " + totalCount);
	            Log.e(TAG, "getShopCarttCountAndPrice: " + totalPrices);
	            businessActivity.refreshShopCartData(totalCount, totalPrices);
	        }
	    }
	
	    /**
	     * @return 获取购物车数据的集合
	     */
	    public List<GoodsInfo> getShopCartList() {
	        ArrayList<GoodsInfo> shopCartList = new ArrayList<>();
	        GoodsFragment goodsFragment = businessActivity.myFragmentPagerAdapter.getGoodsFragment();
	        if (goodsFragment != null) {
	            GoodsAdapter goodsAdapter = goodsFragment.goodsAdapter;
	            //goodsAdapter数据取出来
	            ArrayList<GoodsInfo> goodsInfoList = goodsAdapter.getData();
	            //判断goodsInfoList中每一个对象的count值是否大于0,如果大于0,则此对象需要添加在购物车集合中
	            for (int i = 0; i < goodsInfoList.size(); i++) {
	                GoodsInfo goodsInfo = goodsInfoList.get(i);
	                if (goodsInfo.getCount() > 0) {
	                    shopCartList.add(goodsInfo);
	                }
	            }
	            return shopCartList;
	        }
	        return null;
	    }
	
	    public void clearAll() {
	        //清空商品列表
	        clearGoodsAdapter();
	        //清空商品分类
	        clearGoodsTypeAdapter();
	        //清空购物车
	        clearShopCart();
	        //让对话框隐藏
	        businessActivity.hiddenDialog();
	        //将购物车总数量和总金额都设置为0
	        businessActivity.refreshShopCartData(0, 0.0f);
	    }
	
	    private void clearShopCart() {
	        //因为现在做的操作是清空购物车,购物车的集合就没有存在的必要
	        List<GoodsInfo> shopCartList = getShopCartList();
	        shopCartList.clear();
	    }
	
	    private void clearGoodsTypeAdapter() {
	        GoodsFragment goodsFragment = businessActivity.myFragmentPagerAdapter.getGoodsFragment();
	        if (goodsFragment != null) {
	            GoodsTypeAdapter adapter = goodsFragment.goodsTypeAdapter;
	            List<GoodsTypeInfo> goodsTypeInfoList = adapter.getData();
	            for (int i = 0; i < goodsTypeInfoList.size(); i++) {
	                GoodsTypeInfo goodsTypeInfo = goodsTypeInfoList.get(i);
	                if (goodsTypeInfo.getCount() > 0) {
	                    goodsTypeInfo.setCount(0);
	                }
	            }
	            adapter.notifyDataSetChanged();
	        }
	    }
	
	    private void clearGoodsAdapter() {
	        GoodsFragment goodsFragment = businessActivity.myFragmentPagerAdapter.getGoodsFragment();
	        if (goodsFragment != null) {
	            GoodsAdapter goodsAdapter = goodsFragment.goodsAdapter;
	            //goodsAdapter数据获取出来
	            ArrayList<GoodsInfo> goodsInfoList = goodsAdapter.getData();
	            //判断goodsInfoList中每一个对象的count值是否大于0,如果大于0,则此对象需要添加到购物车集合中
	            for (int i = 0; i < goodsInfoList.size(); i++) {
	                GoodsInfo goodsInfo = goodsInfoList.get(i);
	                if (goodsInfo.getCount() > 0) {
	                    goodsInfo.setCount(0);
	                }
	            }
	            goodsAdapter.notifyDataSetChanged();
	        }
	    }
	}

>购物车中商品在ShopCartRecyclerAdapter类中列表展示

		`public class ShopCartAdapter extends RecyclerView.Adapter {
	    @BindView(R.id.tv_name)
	    TextView tvName;
	    @BindView(R.id.tv_type_all_price)
	    TextView tvTypeAllPrice;
	    @BindView(R.id.ib_minus)
	    ImageButton ibMinus;
	    @BindView(R.id.tv_count)
	    TextView tvCount;
	    @BindView(R.id.ib_add)
	    ImageButton ibAdd;
	    @BindView(R.id.ll)
	    LinearLayout ll;
	    private BusinessActivity businessActivity;
	    private List<GoodsInfo> data;
	
	    public ShopCartAdapter(BusinessActivity businessActivity) {
	        this.businessActivity = businessActivity;
	    }
	
	    @NonNull
	    @Override
	    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
	        View view = View.inflate(parent.getContext(), R.layout.item_cart, null);
	        ViewHolder viewHolder = new ViewHolder(view);
	        return viewHolder;
	    }
	
	    @Override
	    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
	        GoodsInfo goodsInfo = data.get(position);
	        ((ViewHolder) holder).tvName.setText(goodsInfo.getName());
	        ((ViewHolder) holder).tvCount.setText(goodsInfo.getCount() + "");
	        float price = goodsInfo.getCount() * goodsInfo.getNewPrice();
	        ((ViewHolder) holder).tvTypeAllPrice.setText(CountPriceFormater.format(price));
	        ((ViewHolder) holder).setPosition(position);
	    }
	
	    @Override
	    public int getItemCount() {
	        if (data != null && data.size() > 0) {
	            return data.size();
	        }
	        return 0;
	    }
	
	    public void setData(List<GoodsInfo> data) {
	        this.data = data;
	        notifyDataSetChanged();
	    }
	
	
	    class ViewHolder extends RecyclerView.ViewHolder {
	        @BindView(R.id.tv_name)
	        TextView tvName;
	        @BindView(R.id.tv_type_all_price)
	        TextView tvTypeAllPrice;
	        @BindView(R.id.ib_minus)
	        ImageButton ibMinus;
	        @BindView(R.id.tv_count)
	        TextView tvCount;
	        @BindView(R.id.ib_add)
	        ImageButton ibAdd;
	        @BindView(R.id.ll)
	        LinearLayout ll;
	        private int position;
	        private int operation = Constant.ADD;
	
	        ViewHolder(View view) {
	            super(view);
	            ButterKnife.bind(this, view);
	        }
	
	        @OnClick({R.id.ib_minus, R.id.ib_add})
	        public void onViewClicked(View view) {
	            switch (view.getId()) {
	                case R.id.ib_minus:
	                    operation = Constant.DELETE;
	                    //商品里诶包汇总的某件商品数量需要变化-
	                    refreshGoodsAdapterData(operation);
	                    //商品分类数量需要进行变化-
	                    refreshGoodsTypeAdapter(operation);
	                    //如果此商品是减掉了最后一件,则此条目需要从购物车列表中移除
	                    if (data.get(position).getCount() == 0) {
	                        data.remove(position);
	                    }
	                    //购物车里诶博爱中的数据需要进行变化
	                    notifyDataSetChanged();
	//                    如果删除的是所有商品的最后一件,需要将对话框隐藏
	                    if (data.size() == 0) {
	                        hiddenBottomSheetLayout();
	                    }
	                    //更爱购物车中的商品总金额和总数量,在businessPresenter中有计算购物车数据的方法
	                    businessActivity.businessPresenter.getShopCarttCountAndPrice();
	                    break;
	                case R.id.ib_add:
	                    operation = Constant.ADD;
	                    //商品里诶包汇总的某件商品数量需要变化
	                    refreshGoodsAdapterData(operation);
	                    //商品分类数量需要进行变化+
	                    refreshGoodsTypeAdapter(operation);
	                    //购物车列表中的数据需要进行变化+
	                    notifyDataSetChanged();
	                    //更爱购物车中的商品总金额和总数量,在businessPresenter中有计算购物车数据的方法
	                    businessActivity.businessPresenter.getShopCarttCountAndPrice();
	                    break;
	            }
	        }
	
	        /**
	         * 让businessActivity隐藏对话框
	         */
	        private void hiddenBottomSheetLayout() {
	            businessActivity.hiddenDialog();
	        }
	
	        /**
	         * 增加或者减少商品分类选中的数量
	         *
	         * @param operation 操作符  增加或者减少
	         */
	        private void refreshGoodsTypeAdapter(int operation) {
	            //1.获取购物车中需要修改数量商品分类typeid
	            int typeId = data.get(position).getTypeId();
	            //商品分类集合
	            GoodsFragment goodsFragment = businessActivity.myFragmentPagerAdapter.getGoodsFragment();
	            if (goodsFragment != null) {
	                List<GoodsTypeInfo> goodsTypeInfoList = goodsFragment.goodsTypeAdapter.getData();
	                if (goodsTypeInfoList != null) {
	                    for (int i = 0; i < goodsTypeInfoList.size(); i++) {
	                        GoodsTypeInfo goodsTypeInfo = goodsTypeInfoList.get(i);
	                        if (goodsTypeInfo.getId() == typeId) {
	                            switch (operation) {
	                                case Constant.ADD:
	                                    int addCount = goodsTypeInfo.getCount() + 1;
	                                    goodsTypeInfo.setCount(addCount);
	                                    break;
	                                case Constant.DELETE:
	                                    if (goodsTypeInfo.getCount() > 0) {
	
	                                        int deleteCount = goodsTypeInfo.getCount() - 1;
	                                        goodsTypeInfo.setCount(deleteCount);
	                                    }
	                                    break;
	                            }
	                        }
	                    }
	                    goodsFragment.goodsTypeAdapter.notifyDataSetChanged();
	                }
	            }
	        }
	
	        private void refreshGoodsAdapterData(int operation) {
	            //1.获取需要更改商品数量对象的额商品唯一性id
	            int id = data.get(position).getId();
	            //针对id和operation对商品列表中的商品数量进行变更
	            GoodsFragment goodsFragment = businessActivity.myFragmentPagerAdapter.getGoodsFragment();
	            if (goodsFragment != null) {
	                ArrayList<GoodsInfo> goodsInfoArrayList = goodsFragment.goodsAdapter.getData();
	                for (int i = 0; i < goodsInfoArrayList.size(); i++) {
	                    GoodsInfo goodsInfo = goodsInfoArrayList.get(i);
	                    if (goodsInfo.getId() == id) {
	                        switch (operation) {
	                            case Constant.ADD:
	                                int addCount = goodsInfo.getCount() + 1;
	                                goodsInfo.setCount(addCount);
	                                break;
	                            case Constant.DELETE:
	                                if (goodsInfo.getCount() > 0) {
	                                    int deleteCount = goodsInfo.getCount() - 1;
	                                    goodsInfo.setCount(deleteCount);
	                                }
	                                break;
	                        }
	                    }
	                }
	                goodsFragment.goodsAdapter.notifyDataSetChanged();
	            }
	        }
	
	        public void setPosition(int position) {
	            this.position = position;
	        }
	    }
	}
`
	
## 1 商品分类商品详情界面 ##
>商品分类和商品详情分别位于此Fragment的左右2侧,分别采用RecyclerView和StickyListHeadersListView实现所以需要导入2个控件的相关jar包

        implementation 'se.emilsjolander:stickylistheaders:2.7.0'

## 1.1 编写商品界面布局 ##
	
	`public class GoodsFragment extends BaseFragment {
	    @BindView(R.id.rv_goods_type)
	    RecyclerView rvGoodsType;
	    @BindView(R.id.slhlv)
	    StickyListHeadersListView slhlv;
	    Unbinder unbinder;
	    private Seller seller;
	    public GoodsAdapter goodsAdapter;
	    public GoodsTypeAdapter goodsTypeAdapter;
	
	    @Override
	    public void onCreate(@Nullable Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        Bundle bundle = getArguments();
	        //获取数据适配器中通过bundle传递过来的数据
	        seller = (Seller) bundle.getSerializable("seller");
	    }
	
	    @Nullable
	    @Override
	    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
	        View view = View.inflate(getActivity(), R.layout.fragment_goods, null);
	        unbinder = ButterKnife.bind(this, view);
	        return view;
	    }
	
	    @Override
	    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
	        goodsTypeAdapter = new GoodsTypeAdapter(this, getActivity());
	        rvGoodsType.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
	        rvGoodsType.setAdapter(goodsTypeAdapter);
	        goodsAdapter = new GoodsAdapter(this, (BusinessActivity) getActivity());
	        slhlv.setAdapter(goodsAdapter);
	        slhlv.setOnScrollListener(new AbsListView.OnScrollListener() {
	            @Override
	            public void onScrollStateChanged(AbsListView view, int scrollState) {
	
	            }
	
	            @Override
	            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
	                //firstVisibleItem  listview列表课件的第一个条目的索引值
	                //商品列表集合
	                ArrayList<GoodsInfo> goodsInfoList = goodsAdapter.getData();
	                //商品分类集合
	                List<GoodsTypeInfo> goodsTypeInfoList = goodsTypeAdapter.getData();
	                if (goodsInfoList != null && goodsTypeInfoList != null) {
	                    //1.可以通过firstVisibleItem找到右侧列表第一个显示条目的对象  goodsinfo-->typeid
	                    int typeId = goodsInfoList.get(firstVisibleItem).getTypeId();
	                    //找到目前选中的分类索引位置的对象的id和typeid比对,如果不一致,则在后续的循环中找到那个一致的条目为止
	                    int id = goodsTypeInfoList.get(goodsTypeAdapter.currentPosition).getId();
	                    if (typeId != id) {
	                        //2.通过typeId 找到商品分类中的id和typeId一致的那个条目,位置i
	                        for (int i = 0; i < goodsTypeInfoList.size(); i++) {
	                            GoodsTypeInfo goodsTypeInfo = goodsTypeInfoList.get(i);
	                            if (goodsTypeInfo.getId() == typeId) {
	                                //3.让左侧商品分类选中i的这个条目
	                                goodsTypeAdapter.currentPosition = i;
	                                //刷新商品分类数据适配器
	                                goodsTypeAdapter.notifyDataSetChanged();
	                                //因为i可能在屏幕之外,所以需要让左侧的商品分类滚动到i的位置,让用户可以看见
	                                rvGoodsType.smoothScrollToPosition(i);
	                                break;
	                            }
	                        }
	                    }
	                }
	            }
	        });
	        //发送网络请求获取商品列表和分类数据
	        GoodsPresenter goodsPresenter = new GoodsPresenter(goodsTypeAdapter, goodsAdapter, seller);
	        goodsPresenter.getGoodsData(seller.getId());
	        super.onActivityCreated(savedInstanceState);
	    }
	
	    @Override
	    public void onDestroyView() {
	        super.onDestroyView();
	        unbinder.unbind();
	    }
	
	    public void refreshGoodsAdapterUI(int id) {
	        //1.通过此id在商品列表集合中遍历,找到id和typeId一致的那个条目,此条目记录为i
	        ArrayList<GoodsInfo> goodsInfoArrayList = goodsAdapter.getData();
	        if (goodsInfoArrayList != null) {
	            for (int i = 0; i < goodsInfoArrayList.size(); i++) {
	                GoodsInfo goodsInfo = goodsInfoArrayList.get(i);
	                if (goodsInfo.getTypeId() == id) {
	//                    slhlv.smoothScrollToPosition(i);
	                    //2.让listview切换到i位置显示
	                    slhlv.setSelection(i);
	                    break;
	                }
	            }
	        }
	    }
	}`	

## 1.2 发送商品界面网络请求 ##

    public class GoodsPresenter extends BasePresenter {
	    private GoodsAdapter goodsAdapter; //商品列表数据适配器
	    private Seller seller;//商品对象
	    private GoodsTypeAdapter goodsTypeAdapter; //商品分类数据适配器
	    private List<GoodsTypeInfo> goodsTypeInfoList; //商品分类集合
	    private ArrayList<GoodsInfo> allGoodsInfoList; //商品列表集合
	
	    public GoodsPresenter(GoodsTypeAdapter goodsTypeAdapter, GoodsAdapter goodsAdapter, Seller seller) {
	        this.goodsTypeAdapter = goodsTypeAdapter;
	        this.goodsAdapter = goodsAdapter;
	        this.seller = seller;
	    }
	
	    @Override
	    protected void showErrorMessage(String message) {
	
	    }
	
	    @Override
	    protected void parseJson(String data) {
	        Gson gson = new Gson();
	        BusinessInfo businessInfo = gson.fromJson(data, BusinessInfo.class);
	        //1.填充分类的数据适配器列表
	        //2.获取商品分类的数据集合
	        goodsTypeInfoList = businessInfo.getList();
	        goodsTypeAdapter.setData(goodsTypeInfoList);
	        //将分布在每一个分类类中的List里面的对象进行合并,然后填充商品列表数据适配器
	        initGoodsInfoList();
	        //用所有商品所在的大的集合填充商品列表数据适配器
	        goodsAdapter.setData(allGoodsInfoList);
	    }
	
	    private void initGoodsInfoList() {
	        allGoodsInfoList = new ArrayList<>();
	        for (int i = 0; i < goodsTypeInfoList.size(); i++) {
	            //每一个商品分类的bean对象
	            GoodsTypeInfo goodsTypeInfo = goodsTypeInfoList.get(i);
	            //此分类下的所有商品集合
	            List<GoodsInfo> goodsInfoList = goodsTypeInfo.getList();
	            for (int i1 = 0; i1 < goodsInfoList.size(); i1++) {
	                //获取每一个商品对象
	                GoodsInfo goodsInfo = goodsInfoList.get(i1);
	                //指定此商品分类名称
	                goodsInfo.setTypeId(goodsTypeInfo.getId());
	                //指定此商品分类名称
	                goodsInfo.setTypeName(goodsTypeInfo.getName());
	                //指定此商品所属的商家
	                goodsInfo.setSellerId((int) seller.getId());
	                //商品数量
	                //将所有的商品bean对象添加到一个大的集合中
	                allGoodsInfoList.add(goodsInfo);
	            }
	        }
	    }
	
	    //触发请求商品数据方法
	    public void getGoodsData(long id) {
	        Call<ResponseInfo> goodsInfo = responseInfoApi.getGoodsInfo(id);
	        goodsInfo.enqueue(new CallBackAdapter());
	    }
	}

### 1.3 编写相关javabean ###
	
## 2 展示StickyListHeadersListView中数据 ##
### 2.1 准备数据源 ###

    
    private void initGoodsInfoList() {
        allGoodsInfoList = new ArrayList<>();
        for (int i = 0; i < goodsTypeInfoList.size(); i++) {
            //每一个商品分类的bean对象
            GoodsTypeInfo goodsTypeInfo = goodsTypeInfoList.get(i);
            //此分类下的所有商品集合
            List<GoodsInfo> goodsInfoList = goodsTypeInfo.getList();
            for (int i1 = 0; i1 < goodsInfoList.size(); i1++) {
                //获取每一个商品对象
                GoodsInfo goodsInfo = goodsInfoList.get(i1);
                //指定此商品分类名称
                goodsInfo.setTypeId(goodsTypeInfo.getId());
                //指定此商品分类名称
                goodsInfo.setTypeName(goodsTypeInfo.getName());
                //指定此商品所属的商家
                goodsInfo.setSellerId((int) seller.getId());
                //商品数量
                //将所有的商品bean对象添加到一个大的集合中
                allGoodsInfoList.add(goodsInfo);
            }
        }
    }

### 2.2 填充数据适配器 ###

    public class GoodsAdapter extends BaseAdapter implements StickyListHeadersAdapter {
	    private GoodsFragment goodsFragment;
	    private BusinessActivity businessActivity;
	    private ArrayList<GoodsInfo> data;
	
	    public GoodsAdapter(GoodsFragment goodsFragment, BusinessActivity businessActivity) {
	        this.goodsFragment = goodsFragment;
	        this.businessActivity = businessActivity;
	    }
	
	    @Override
	    public View getHeaderView(int position, View convertView, ViewGroup parent) {
	        //返回头的view对象
	        if (convertView == null) {
	            convertView = View.inflate(parent.getContext(), R.layout.item_type_header, null);
	        }
	        GoodsInfo goodsInfo = getItem(position);
	        //给每一个商品设置分类名称头
	        ((TextView) convertView).setText(goodsInfo.getTypeName());
	        return convertView;
	    }
	
	    @Override
	    public long getHeaderId(int position) {
	        //返回值情况有几种,就有几个头
	        return data.get(position).getTypeId();//0到data.size-1
	    }
	
	    @Override
	    public int getCount() {
	        if (data != null && data.size() > 0) {
	            return data.size();
	        }
	        return 0;
	    }
	
	    @Override
	    public GoodsInfo getItem(int position) {
	        return data.get(position);
	    }
	
	    @Override
	    public long getItemId(int position) {
	        return position;
	    }
	
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        ViewHolder viewHolder = null;
	        if (convertView == null) {
	            convertView = View.inflate(parent.getContext(), R.layout.item_goods, null);
	            viewHolder = new ViewHolder(convertView);
	            convertView.setTag(viewHolder);
	        } else {
	            viewHolder = (ViewHolder) convertView.getTag();
	        }
	        GoodsInfo goodsInfo = getItem(position);
	        //设置商品名称
	        viewHolder.tvName.setText(goodsInfo.getName());
	        //设置金额
	        String strNewPrice = CountPriceFormater.format(goodsInfo.getNewPrice());
	        String strOldPrice = CountPriceFormater.format(goodsInfo.getOldPrice());
	        viewHolder.tvNewprice.setText(strNewPrice);
	        viewHolder.tvOldprice.setText(strOldPrice);
	        //设置图片
	        Picasso.with(parent.getContext()).load(goodsInfo.getIcon()).into(viewHolder.ivIcon);
	        //显示选中商品的数量
	        if (goodsInfo.getCount() > 0) {
	            viewHolder.tvCount.setVisibility(View.VISIBLE);
	            viewHolder.tvCount.setText(goodsInfo.getCount() + "");
	            viewHolder.ibMinus.setVisibility(View.VISIBLE);
	        } else {
	            viewHolder.tvCount.setVisibility(View.GONE);
	            viewHolder.ibMinus.setVisibility(View.GONE);
	        }
	        viewHolder.setPosition(position);
	        return convertView;
	    }
	
	    public void setData(ArrayList<GoodsInfo> data) {
	        this.data = data;
	        notifyDataSetChanged();
	    }
	
	    /**
	     * @return 商品列表集合
	     */
	    public ArrayList<GoodsInfo> getData() {
	        return data;
	    }
	
	    class ViewHolder {
	        @BindView(R.id.iv_icon)
	        ImageView ivIcon;
	        @BindView(R.id.tv_name)
	        TextView tvName;
	        @BindView(R.id.tv_zucheng)
	        TextView tvZucheng;
	        @BindView(R.id.tv_yueshaoshounum)
	        TextView tvYueshaoshounum;
	        @BindView(R.id.tv_newprice)
	        TextView tvNewprice;
	        @BindView(R.id.tv_oldprice)
	        TextView tvOldprice;
	        @BindView(R.id.ib_minus)
	        ImageButton ibMinus;
	        @BindView(R.id.tv_count)
	        TextView tvCount;
	        @BindView(R.id.ib_add)
	        ImageButton ibAdd;
	        private int position;
	        private int operation = Constant.ADD;
	
	        ViewHolder(View view) {
	            ButterKnife.bind(this, view);
	        }
	
	        @OnClick({R.id.ib_add, R.id.ib_minus})
	        public void onViewClick(View view) {
	            switch (view.getId()) {
	                case R.id.ib_add:
	                    //点击完+号后,则控件不可用
	                    view.setEnabled(false);
	                    addGoods(view);
	                    operation = Constant.ADD;
	                    break;
	                case R.id.ib_minus:
	                    deleteGoods();
	                    operation = Constant.DELETE;
	                    break;
	            }
	            //1.如果对商品的数量进行了增加或者减少,则需要告知分类数量进行变更
	            //修改分类数量(说明分类的数量增加还是减少,哪个分类数量需要变化
	            int typeId = data.get(position).getTypeId();
	            //2.通知商品分类数量进行变化
	            goodsFragment.goodsTypeAdapter.refreshUI(typeId, operation);
	            //3.更改购物中商品数量和总金额
	            businessActivity.businessPresenter.getShopCarttCountAndPrice();
	        }
	
	        /**
	         * 减少一件商品
	         */
	        private void deleteGoods() {
	            //选中条目对象count值为1的时候,才有如下动画
	            if (data.get(position).getCount() == 1) {
	                //旋转
	                RotateAnimation rotateAnimation = new RotateAnimation(720, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
	                //透明度变化
	                AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.5f);
	                //平移(x轴  正值--->0)
	                TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 2.0f,
	                        Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
	                AnimationSet animationSet = new AnimationSet(false);
	                animationSet.addAnimation(rotateAnimation);
	                animationSet.addAnimation(alphaAnimation);
	                animationSet.addAnimation(translateAnimation);
	                animationSet.setDuration(300);
	                ibMinus.startAnimation(animationSet);
	                //减号一出去的动画执行完成后,需要将括号和数量在此隐藏
	                animationSet.setAnimationListener(new Animation.AnimationListener() {
	                    @Override
	                    public void onAnimationStart(Animation animation) {
	
	                    }
	
	                    @Override
	                    public void onAnimationEnd(Animation animation) {
	                        tvCount.setVisibility(View.GONE);
	                        ibMinus.setVisibility(View.GONE);
	                        //数量归0操作
	                        data.get(position).setCount(0);
	                        //因为数量和减号控件已经被隐藏了(等同于刷新数据适配器效果
	                        //所以数据适配器没必要刷新
	                        businessActivity.businessPresenter.getShopCarttCountAndPrice();
	                    }
	
	                    @Override
	                    public void onAnimationRepeat(Animation animation) {
	
	                    }
	                });
	            } else {
	                //数量减少
	                //用户点击+号添加意见商品后,需要刷新数据适配器更新商品数量
	                int deleteCount = data.get(position).getCount() - 1;
	                data.get(position).setCount(deleteCount);
	                notifyDataSetChanged();
	            }
	        }
	
	        /**
	         * @param view 添加一件商品
	         */
	        private void addGoods(View view) {
	            //选中条目对象count的值为0的时候,才有如下动画
	            if (data.get(position).getCount() == 0) {
	                //旋转
	                RotateAnimation rotateAnimation = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
	                //透明度变化
	                AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 1.0f);
	                //平移(x轴,正值-------->0)
	                TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 2.0f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
	                AnimationSet animationSet = new AnimationSet(false);
	                animationSet.addAnimation(rotateAnimation);
	                animationSet.addAnimation(alphaAnimation);
	                animationSet.addAnimation(translateAnimation);
	                animationSet.setDuration(300);
	                ibMinus.startAnimation(animationSet);
	                //减号和数量控件必须显示,才可以让动画效果可见
	                ibMinus.setVisibility(View.VISIBLE);
	                tvCount.setVisibility(View.VISIBLE);
	            }
	            //用户点击+号添加一件商品,需要刷新数据适配器更新商品数量
	            int addCount = data.get(position).getCount() + 1;
	            data.get(position).setCount(addCount);
	            notifyDataSetChanged();
	            //点击加号时抛物线动画效果
	            addFlyImage(view);
	        }
	
	        private void addFlyImage(View view) {
	            //1.用户飞行小球
	            ImageView imageview = new ImageView(businessActivity);
	            imageview.setBackgroundResource(R.mipmap.button_add);
	            //2.制定飞行小球的开始位置
	            int[] viewLocation = new int[2];
	            view.getLocationInWindow(viewLocation);
	            //3.让imageview和view的所在位置重合
	            imageview.setX(viewLocation[0]);
	            imageview.setY(viewLocation[1] - MyApplication.statusBarHeight);
	            //4.并且将小球需要添加在帧布局的页面中
	            ((BusinessActivity) businessActivity).addFlyImageView(imageview, view.getWidth(), view.getHeight());
	            //5.获取飞行起点和终点的坐标
	            int[] desLocation = ((BusinessActivity) businessActivity).getShopCartLocation();
	            int[] sourceLocation = new int[2];
	            imageview.getLocationInWindow(sourceLocation);
	            //6.飞
	            fly(imageview, view, sourceLocation, desLocation);
	        }
	
	        private void fly(final ImageView imageview, final View view, int[] sourceLocation, int[] desLocation) {
	            //1.x轴起飞位置和终点位置
	            int startX = sourceLocation[0];
	            int endX = desLocation[0];
	            //2.y轴其实位置和终点位置
	            int startY = sourceLocation[1];
	            int endY = desLocation[1];
	            //3.管理x轴飞行,匀速
	            TranslateAnimation translateAnimationX = new TranslateAnimation(Animation.ABSOLUTE, 0, Animation.ABSOLUTE, endX - startX,
	                    Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0);
	            translateAnimationX.setInterpolator(new LinearInterpolator());
	            //3.管理y轴飞行
	            TranslateAnimation translateAnimationY = new TranslateAnimation(
	                    Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0,
	                    Animation.ABSOLUTE, 0, Animation.ABSOLUTE, endY - startY);
	            translateAnimationY.setInterpolator(new AccelerateInterpolator());
	            //将2个动画合并
	            AnimationSet animationSet = new AnimationSet(false);
	            animationSet.addAnimation(translateAnimationX);
	            animationSet.addAnimation(translateAnimationY);
	            animationSet.setDuration(500);
	            imageview.startAnimation(animationSet);
	            animationSet.setAnimationListener(new Animation.AnimationListener() {
	                @Override
	                public void onAnimationStart(Animation animation) {
	
	                }
	
	                @Override
	                public void onAnimationEnd(Animation animation) {
	                    imageview.setVisibility(View.GONE);
	                    //飞行动画结束后控件可用
	                    view.setEnabled(true);
	                }
	
	                @Override
	                public void onAnimationRepeat(Animation animation) {
	
	                }
	            });
	        }
	
	        public void setPosition(int position) {
	            this.position = position;
	        }
	    }
	}

### 2.3 设置价格带¥ ###
	
    public class CountPriceFormater {
	    private CountPriceFormater() {
	
	    }
	
	    public static String format(float countPrice) {
	        NumberFormat format = NumberFormat.getCurrencyInstance();
	        format.setMaximumFractionDigits(2);
	        return format.format(countPrice);
	    }
	}

## 3 左右2侧的列表数据联动 ##
### 3.1 左侧条目选中索引变化------->右侧索引变化  ###

    
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //设置分类标题
        ((ViewHolder) holder).type.setText(data.get(position).getName());
        //设置分类数量,气泡显示隐藏
        int count = data.get(position).getCount();
        if (count == 0) {
            //隐藏气泡
            ((ViewHolder) holder).tvCount.setVisibility(View.GONE);
        } else {
            //显示气泡,并且显示数量
            ((ViewHolder) holder).tvCount.setVisibility(View.VISIBLE);
            ((ViewHolder) holder).tvCount.setText(count + "");
        }
        if (currentPosition == position) {
            //将viewholder中的布局文件转换成的view对象背景设置为白色
            holder.itemView.setBackgroundColor(Color.WHITE);
            //显示分类的textview颜色红
            ((ViewHolder) holder).type.setTextColor(Color.RED);
        } else {
            //将viewholder中的布局文件转换成的view对象背景设置为灰色
            holder.itemView.setBackgroundColor(Color.LTGRAY);
            //显示分类的textview颜色黑
            ((ViewHolder) holder).type.setTextColor(Color.BLACK);
        }
        ((ViewHolder) holder).setPosition(position);
    }
	

	GoodsFragment中点击事件
	
	
            slhlv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //firstVisibleItem  listview列表课件的第一个条目的索引值
                //商品列表集合
                ArrayList<GoodsInfo> goodsInfoList = goodsAdapter.getData();
                //商品分类集合
                List<GoodsTypeInfo> goodsTypeInfoList = goodsTypeAdapter.getData();
                if (goodsInfoList != null && goodsTypeInfoList != null) {
                    //1.可以通过firstVisibleItem找到右侧列表第一个显示条目的对象  goodsinfo-->typeid
                    int typeId = goodsInfoList.get(firstVisibleItem).getTypeId();
                    //找到目前选中的分类索引位置的对象的id和typeid比对,如果不一致,则在后续的循环中找到那个一致的条目为止
                    int id = goodsTypeInfoList.get(goodsTypeAdapter.currentPosition).getId();
                    if (typeId != id) {
                        //2.通过typeId 找到商品分类中的id和typeId一致的那个条目,位置i
                        for (int i = 0; i < goodsTypeInfoList.size(); i++) {
                            GoodsTypeInfo goodsTypeInfo = goodsTypeInfoList.get(i);
                            if (goodsTypeInfo.getId() == typeId) {
                                //3.让左侧商品分类选中i的这个条目
                                goodsTypeAdapter.currentPosition = i;
                                //刷新商品分类数据适配器
                                goodsTypeAdapter.notifyDataSetChanged();
                                //因为i可能在屏幕之外,所以需要让左侧的商品分类滚动到i的位置,让用户可以看见
                                rvGoodsType.smoothScrollToPosition(i);
                                break;
                            }
                        }
                    }
                }
            }
        });

### 3.2 右侧条目滚动-------->影响左侧条目指向索引 ###
	
	监听右侧列表的滚动,获取滚动到第一个条目的索引,通过此索引获取指向商品的类型id,即TypeId,让此TypeId和左侧列表的类型id匹配
	如果一致则不需要再让左侧列表滚动,否则需要让左侧列表滚动至和右侧类型id一致的条目
		
            slhlv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //firstVisibleItem  listview列表课件的第一个条目的索引值
                //商品列表集合
                ArrayList<GoodsInfo> goodsInfoList = goodsAdapter.getData();
                //商品分类集合
                List<GoodsTypeInfo> goodsTypeInfoList = goodsTypeAdapter.getData();
                if (goodsInfoList != null && goodsTypeInfoList != null) {
                    //1.可以通过firstVisibleItem找到右侧列表第一个显示条目的对象  goodsinfo-->typeid
                    int typeId = goodsInfoList.get(firstVisibleItem).getTypeId();
                    //找到目前选中的分类索引位置的对象的id和typeid比对,如果不一致,则在后续的循环中找到那个一致的条目为止
                    int id = goodsTypeInfoList.get(goodsTypeAdapter.currentPosition).getId();
                    if (typeId != id) {
                        //2.通过typeId 找到商品分类中的id和typeId一致的那个条目,位置i
                        for (int i = 0; i < goodsTypeInfoList.size(); i++) {
                            GoodsTypeInfo goodsTypeInfo = goodsTypeInfoList.get(i);
                            if (goodsTypeInfo.getId() == typeId) {
                                //3.让左侧商品分类选中i的这个条目
                                goodsTypeAdapter.currentPosition = i;
                                //刷新商品分类数据适配器
                                goodsTypeAdapter.notifyDataSetChanged();
                                //因为i可能在屏幕之外,所以需要让左侧的商品分类滚动到i的位置,让用户可以看见
                                rvGoodsType.smoothScrollToPosition(i);
                                break;
                            }
                        }
                    }
                }
            }
        });


## 1 创建地址的javabean ##
>创建存储地址的javabean,和存储地址的表

    
	@DatabaseTable(tableName = "t_receiptaddress")
	public class ReceiptAddressBean implements Serializable {
	    @DatabaseField(generatedId = true)
	    private int _id;
	    @DatabaseField(columnName = "uid")
	    private int uid;
	    @DatabaseField(columnName = "name")
	    private String name;
	    @DatabaseField(columnName = "sex")
	    private String sex;
	    @DatabaseField(columnName = "phone")
	    private String phone;
	    @DatabaseField(columnName = "phoneOther")
	    private String phoneOther;
	    @DatabaseField(columnName = "receiptAddress")
	    private String receiptAddress;
	    @DatabaseField(columnName = "detailAddress")
	    private String detailAddress;
	    @DatabaseField(columnName = "label")
	    private String label;
	    @DatabaseField(columnName = "isSelect")
	    private int isSelect;//1代表被选中,0代表没被选中
	
	    public ReceiptAddressBean(int uid, String name, String sex, String phone, String phoneOther,
	                              String receiptAddress, String detailAddress, String label, int isSelect) {
	        this.uid = uid;
	        this.name = name;
	        this.sex = sex;
	        this.phone = phone;
	        this.phoneOther = phoneOther;
	        this.receiptAddress = receiptAddress;
	        this.detailAddress = detailAddress;
	        this.label = label;
	        this.isSelect = isSelect;
	    }
	
	    public ReceiptAddressBean() {
	
	    }
	
	    public int get_id() {
	        return _id;
	    }
	
	    public void set_id(int _id) {
	        this._id = _id;
	    }
	
	    public int getUid() {
	        return uid;
	    }
	
	    public void setUid(int uid) {
	        this.uid = uid;
	    }
	
	    public String getName() {
	        return name;
	    }
	
	    public void setName(String name) {
	        this.name = name;
	    }
	
	    public String getSex() {
	        return sex;
	    }
	
	    public void setSex(String sex) {
	        this.sex = sex;
	    }
	
	    public String getPhone() {
	        return phone;
	    }
	
	    public void setPhone(String phone) {
	        this.phone = phone;
	    }
	
	    public String getPhoneOther() {
	        return phoneOther;
	    }
	
	    public void setPhoneOther(String phoneOther) {
	        this.phoneOther = phoneOther;
	    }
	
	    public String getReceiptAddress() {
	        return receiptAddress;
	    }
	
	    public void setReceiptAddress(String receiptAddress) {
	        this.receiptAddress = receiptAddress;
	    }
	
	    public String getDetailAddress() {
	        return detailAddress;
	    }
	
	    public void setDetailAddress(String detailAddress) {
	        this.detailAddress = detailAddress;
	    }
	
	    public String getLabel() {
	        return label;
	    }
	
	    public void setLabel(String label) {
	        this.label = label;
	    }
	
	    public int isSelect() {
	        return isSelect;
	    }
	
	    public void setSelect(int select) {
	        isSelect = select;
	    }
	}

## 2 提供DAO层地址的增删改查方法 ##

    
	public class ReceiptAddressDao {
	    private DBHelper dbHelper;
	    private Dao<ReceiptAddressBean, Integer> dao;
	
	    public ReceiptAddressDao(Context ctx) {
	        dbHelper = new DBHelper(ctx);
	        dao = dbHelper.getDao(ReceiptAddressBean.class);
	    }
	
	    public void create(ReceiptAddressBean receiptAddressBean) {
	        try {
	            dao.create(receiptAddressBean);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	
	    public void delete(ReceiptAddressBean receiptAddressBean) {
	        try {
	            dao.delete(receiptAddressBean);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	
	    public void update(ReceiptAddressBean receiptAddressBean) {
	        try {
	            dao.update(receiptAddressBean);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	
	    /**
	     * @param uid
	     * @return 查询当前登录用户的所有地址
	     */
	    public List<ReceiptAddressBean> queryUserAddress(int uid) {
	        try {
	            List<ReceiptAddressBean> addressList = dao.queryBuilder().where().eq("uid", uid).query();
	            return addressList;
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	
	    /**
	     * @param uid
	     * @return 查询当前用户登录的的默认送货地址
	     */
	    public ReceiptAddressBean queryUserSelectAddress(int uid) {
	        try {
	            List<ReceiptAddressBean> addressList = dao.queryBuilder().where().eq("uid", uid).and().eq("isSelect", 1).query();
	            if (addressList != null && addressList.size() > 0) {
	                ReceiptAddressBean receiptAddressBean = addressList.get(0);
	                return receiptAddressBean;
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	}

## 3 地址列表 ##

    
	public class AddressListActivity extends Activity {
	    @BindView(R.id.ib_back)
	    ImageButton ibBack;
	    @BindView(R.id.tv_title)
	    TextView tvTitle;
	    @BindView(R.id.rv_receipt_address)
	    RecyclerView rvReceiptAddress;
	    @BindView(R.id.tv_add_address)
	    TextView tvAddAddress;
	
	    @Override
	    protected void onCreate(@Nullable Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_receipt_address);
	        ButterKnife.bind(this);
	    }
	
	    @Override
	    protected void onResume() {
	        //查询数据库中登录用户的所有地址列表展示
	        ReceiptAddressDao receiptAddressDao = new ReceiptAddressDao(this);
	        List<ReceiptAddressBean> receiptAddressList = receiptAddressDao.queryUserAddress(MyApplication.userId);
	        if (receiptAddressList != null && receiptAddressList.size() > 0) {
	            //receiptAddressList就是登录用户的地址
	            AddressListAdapter addressListAdapter = new AddressListAdapter(this, receiptAddressList);
	            rvReceiptAddress.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
	            rvReceiptAddress.setAdapter(addressListAdapter);
	        }
	        super.onResume();
	    }
	
	    @OnClick(R.id.tv_add_address)
	    public void onViewClicked() {
	        Intent intent = new Intent(this, AddAddressActivity.class);
	        startActivity(intent);
	    }
	}

## 4 地址列表数据适配器 ##

    
	public class AddressListAdapter extends RecyclerView.Adapter {
	    private Activity activity;
	    private String[] addressLabels;
	    private int[] bgLabels;
	    private List<ReceiptAddressBean> data;
	    private final ReceiptAddressDao receiptAddressDao;
	    private static final String TAG = "AddressListAdapter";
	
	    public AddressListAdapter(Activity activity, List<ReceiptAddressBean> data) {
	        this.activity = activity;
	        this.data = data;
	        receiptAddressDao = new ReceiptAddressDao(activity);
	        addressLabels = new String[]{"家", "公司", "学校"};
	        bgLabels = new int[]{Color.parseColor("#fc7251"), Color.parseColor("#468ade"), Color.parseColor("#02c14b")};
	    }
	
	    @NonNull
	    @Override
	    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
	        View view = View.inflate(parent.getContext(), R.layout.item_receipt_address, null);
	        ViewHolder viewHolder = new ViewHolder(view);
	        return viewHolder;
	    }
	
	    @Override
	    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
	        ReceiptAddressBean receiptAddressBean = data.get(position);
	        //设置名称
	        ((ViewHolder) holder).tvName.setText(receiptAddressBean.getName());
	        ((ViewHolder) holder).tvSex.setText(receiptAddressBean.getSex());
	        //有2个号码可以显示
	        if (!TextUtils.isEmpty(receiptAddressBean.getPhone()) && !TextUtils.isEmpty(receiptAddressBean.getPhoneOther())) {
	            ((ViewHolder) holder).tvPhone.setText(receiptAddressBean.getPhone() + "," + receiptAddressBean.getPhoneOther());
	        }
	        //有一个号码可以显示
	        if (!TextUtils.isEmpty(receiptAddressBean.getPhone()) && TextUtils.isEmpty(receiptAddressBean.getPhoneOther())) {
	            ((ViewHolder) holder).tvPhone.setText(receiptAddressBean.getPhone());
	        }
	        //显示地址是公司,还是学校,还是家
	        ((ViewHolder) holder).tvLabel.setText(receiptAddressBean.getLabel());
	        //获取label后需要获取数组的索引位置,通过索引位置获取tvLabel控件的背景色
	        Log.e(TAG, "onBindViewHolder: " + receiptAddressBean.getLabel());
	        if (receiptAddressBean.getLabel() != null && !receiptAddressBean.getLabel().equals("")) {
	            int index = getIndex(receiptAddressBean.getLabel());
	            ((ViewHolder) holder).tvLabel.setBackgroundColor(bgLabels[index]);
	            Log.e(TAG, "onBindViewHolder: " + index);
	        }
	        //显示初略地址和详细地址的合并结果
	        ((ViewHolder) holder).tvAddress.setText(receiptAddressBean.getReceiptAddress() + receiptAddressBean.getDetailAddress());
	        ((ViewHolder) holder).setPosition(position);
	        //判断receiptAddressBean对象中isselect值是否为1,决定前面√是否出现
	        if (receiptAddressBean.isSelect() == 1) {
	            //显示√
	            ((ViewHolder) holder).cb.setVisibility(View.VISIBLE);
	            ((ViewHolder) holder).cb.setChecked(true);
	        } else {
	            //隐鲹√
	            ((ViewHolder) holder).cb.setVisibility(View.GONE);
	            ((ViewHolder) holder).cb.setChecked(false);
	        }
	    }
	
	    private int getIndex(String label) {
	        int index = -1;
	        for (int i = 0; i < addressLabels.length; i++) {
	            if (addressLabels[i].equals(label)) {
	                index = i;
	                break;
	            }
	        }
	        return index;
	    }
	
	    @Override
	    public int getItemCount() {
	        return data.size();
	    }
	
	    class ViewHolder extends RecyclerView.ViewHolder {
	        @BindView(R.id.cb)
	        CheckBox cb;
	        @BindView(R.id.tv_name)
	        TextView tvName;
	        @BindView(R.id.tv_sex)
	        TextView tvSex;
	        @BindView(R.id.tv_phone)
	        TextView tvPhone;
	        @BindView(R.id.tv_label)
	        TextView tvLabel;
	        @BindView(R.id.tv_address)
	        TextView tvAddress;
	        @BindView(R.id.iv_edit)
	        ImageView ivEdit;
	        private int position;
	
	        ViewHolder(View view) {
	            super(view);
	            ButterKnife.bind(this, view);
	            ivEdit.setOnClickListener(new View.OnClickListener() {
	                @Override
	                public void onClick(View v) {
	                    ReceiptAddressBean receiptAddressBean = data.get(position);
	                    Intent intent = new Intent(activity, AddAddressActivity.class);
	                    intent.putExtra("receiptAddressBean", receiptAddressBean);
	                    activity.startActivity(intent);
	                }
	            });
	            view.setOnClickListener(new View.OnClickListener() {
	                @Override
	                public void onClick(View v) {
	                    //循环遍历data地址集合
	                    for (int i = 0; i < data.size(); i++) {
	                        ReceiptAddressBean receiptAddressBean = data.get(i);
	                        if (i == position) {
	                            //找到了选中的条目,要将选中条目对象isselect值修改为1
	                            receiptAddressBean.setSelect(1);
	                        } else {
	                            //其余的条目都是没有选中的,要将未选中条目对象的isselect值修改为0
	                            receiptAddressBean.setSelect(0);
	                        }
	                        //更新数据值数据库中
	                        receiptAddressDao.update(receiptAddressBean);
	                    }
	                    notifyDataSetChanged();
	                    activity.finish();
	                }
	            });
	        }
	
	        public void setPosition(int position) {
	            this.position = position;
	        }
	    }
	}

## 5 增加地址&修改地址 ##

    
	public class AddAddressActivity extends Activity implements View.OnFocusChangeListener {
	    @BindView(R.id.ib_back)
	    ImageButton ibBack;
	    @BindView(R.id.tv_title)
	    TextView tvTitle;
	    @BindView(R.id.ib_delete)
	    ImageButton ibDelete;
	    @BindView(R.id.tv_name)
	    TextView tvName;
	    @BindView(R.id.et_name)
	    EditText etName;
	    @BindView(R.id.rb_man)
	    RadioButton rbMan;
	    @BindView(R.id.rb_women)
	    RadioButton rbWomen;
	    @BindView(R.id.rg_sex)
	    RadioGroup rgSex;
	    @BindView(R.id.et_phone)
	    EditText etPhone;
	    @BindView(R.id.ib_delete_phone)
	    ImageButton ibDeletePhone;
	    @BindView(R.id.ib_add_phone_other)
	    ImageButton ibAddPhoneOther;
	    @BindView(R.id.et_phone_other)
	    EditText etPhoneOther;
	    @BindView(R.id.ib_delete_phone_other)
	    ImageButton ibDeletePhoneOther;
	    @BindView(R.id.rl_phone_other)
	    RelativeLayout rlPhoneOther;
	    @BindView(R.id.tv_receipt_address)
	    TextView tvReceiptAddress;
	    @BindView(R.id.et_detail_address)
	    EditText etDetailAddress;
	    @BindView(R.id.tv_label)
	    TextView tvLabel;
	    @BindView(R.id.ib_select_label)
	    ImageView ibSelectLabel;
	    @BindView(R.id.bt_ok)
	    Button btOk;
	    private ReceiptAddressDao receiptAddressDao;
	    private String[] addressLabels;
	    private int[] bgLabels;
	    private ReceiptAddressBean receiptAddressBean;
	    private static final String TAG = "AddAddressActivity";
	
	    @Override
	    protected void onCreate(@Nullable Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_add_edit_receipt_address);
	        ButterKnife.bind(this);
	        addressLabels = new String[]{"家", "公司", "学校"};
	        bgLabels = new int[]{Color.parseColor("#fc7251"), Color.parseColor("#468ade"), Color.parseColor("#02c14b")};
	        receiptAddressDao = new ReceiptAddressDao(this);
	        //edittext中文本内容监听
	        etPhone.addTextChangedListener(new TextWatcher() {
	            @Override
	            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	
	            }
	
	            @Override
	            public void onTextChanged(CharSequence s, int start, int before, int count) {
	
	            }
	
	            @Override
	            public void afterTextChanged(Editable s) {
	                String phone = etPhone.getText().toString();
	                if (TextUtils.isEmpty(phone)) {
	                    ibDeletePhone.setVisibility(View.GONE);
	                } else {
	                    ibDeletePhone.setVisibility(View.VISIBLE);
	                }
	            }
	        });
	
	        etPhoneOther.addTextChangedListener(new TextWatcher() {
	            @Override
	            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	
	            }
	
	            @Override
	            public void onTextChanged(CharSequence s, int start, int before, int count) {
	
	            }
	
	            @Override
	            public void afterTextChanged(Editable s) {
	                String phone = etPhoneOther.getText().toString();
	                if (TextUtils.isEmpty(phone)) {
	                    ibDeletePhoneOther.setVisibility(View.GONE);
	                } else {
	                    ibDeletePhoneOther.setVisibility(View.VISIBLE);
	                }
	            }
	        });
	        //对edittext中的焦点是否存在监听
	        etPhone.setOnFocusChangeListener(this);
	        etPhoneOther.setOnFocusChangeListener(this);
	        //通过编辑按钮跳转到此界面传递过来的地址对象
	        receiptAddressBean = (ReceiptAddressBean) getIntent().getSerializableExtra("receiptAddressBean");
	        //显示receiptAddressBean中的内容
	        showReceiptAddress(receiptAddressBean);
	    }
	
	    private void showReceiptAddress(ReceiptAddressBean receiptAddressBean) {
	        if (receiptAddressBean != null) {
	            etName.setText(receiptAddressBean.getName());
	            String sex = receiptAddressBean.getSex();
	            if (sex.equals("男性")) {
	                rgSex.check(R.id.rb_man);
	            } else {
	                rgSex.check(R.id.rb_women);
	            }
	            etPhone.setText(receiptAddressBean.getPhone());
	            etPhoneOther.setText(receiptAddressBean.getPhoneOther());
	            tvReceiptAddress.setText(receiptAddressBean.getReceiptAddress());
	            etDetailAddress.setText(receiptAddressBean.getDetailAddress());
	            tvLabel.setText(receiptAddressBean.getLabel());
	            int index = getIndex(receiptAddressBean.getLabel());
	            tvLabel.setBackgroundColor(bgLabels[index]);
	        }
	    }
	
	    private int getIndex(String label) {
	        int index = -1;
	        for (int i = 0; i < addressLabels.length; i++) {
	            if (addressLabels[i].equals(label)) {
	                index = i;
	                break;
	            }
	        }
	        return index;
	    }
	
	    @Override
	    public void onFocusChange(View v, boolean hasFocus) {
	        //edittext焦点发生变化时调用方法,v 及时edittext对象本身,hasfoucus对象是否有焦点
	        if (v.getId() == R.id.et_phone) {
	            String phone = etPhone.getText().toString();
	            if (hasFocus && !TextUtils.isEmpty(phone)) {
	                //既有文本,又有焦点
	                ibDeletePhone.setVisibility(View.VISIBLE);
	            } else {
	                ibDeletePhone.setVisibility(View.GONE);
	            }
	        } else {
	            String phone = etPhoneOther.getText().toString();
	            if (hasFocus && !TextUtils.isEmpty(phone)) {
	                //既有文本,又有焦点
	                ibDeletePhoneOther.setVisibility(View.VISIBLE);
	            } else {
	                ibDeletePhoneOther.setVisibility(View.GONE);
	            }
	        }
	    }
	
	    @OnClick({R.id.ib_delete, R.id.ib_add_phone_other, R.id.ib_select_label, R.id.bt_ok})
	    public void onViewClicked(View view) {
	        switch (view.getId()) {
	            case R.id.ib_delete:
	                if (receiptAddressBean != null) {
	                    //删除receiptAddressBean对象,从数据库中删除receiptAddressBean指向的的数据
	                    receiptAddressDao.delete(receiptAddressBean);
	                    finish();
	                }
	                break;
	            case R.id.ib_add_phone_other:
	                if (rlPhoneOther.getVisibility() == View.VISIBLE) {
	                    rlPhoneOther.setVisibility(View.GONE);
	                } else {
	                    rlPhoneOther.setVisibility(View.VISIBLE);
	                }
	                break;
	            case R.id.ib_select_label:
	                showLabelDialog();
	                break;
	            case R.id.bt_ok:
	                Log.e(TAG, "onViewClicked: 1111");
	                if (checkData()) {
	                    if (receiptAddressBean == null) {
	                        //数据合法,将数据获取出来放置到bean中,然后将bean插入数据库中
	                        createReceiptAddress();
	                    } else {
	                        updateReceiptAddress();
	                    }
	                }
	                break;
	        }
	    }
	
	    private void updateReceiptAddress() {
	        //如何将编辑后的呢日用,更新到数据库中
	        //重新获取界面控件中编辑后的内容
	        String name = etName.getText().toString().trim();
	        String phone = etPhone.getText().toString().trim();
	        String otherPhone = etPhoneOther.getText().toString().trim();
	        String receiptAddress = tvReceiptAddress.getText().toString().trim();
	        String detailAddress = etDetailAddress.getText().toString().trim();
	        String tvLabelString = tvLabel.getText().toString();
	        int checkId = rgSex.getCheckedRadioButtonId();
	        String sex = "";
	        if (checkId == R.id.rb_man) {
	            sex = "男性";
	        } else {
	            sex = "女性";
	        }
	        //将编辑后的内容设置在receiptAddressBean对象中
	        receiptAddressBean.setName(name);
	        receiptAddressBean.setPhone(phone);
	        receiptAddressBean.setPhoneOther(otherPhone);
	        receiptAddressBean.setReceiptAddress(receiptAddress);
	        receiptAddressBean.setDetailAddress(detailAddress);
	        receiptAddressBean.setLabel(tvLabelString);
	        receiptAddressBean.setSex(sex);
	        //通过receiptAddressDao更新数据库
	        receiptAddressDao.update(receiptAddressBean);
	        finish();
	    }
	
	    private void createReceiptAddress() {
	        String name = etName.getText().toString().trim();
	        String phone = etPhone.getText().toString().trim();
	        String otherPhone = etPhoneOther.getText().toString().trim();
	        String receiptAddress = tvReceiptAddress.getText().toString().trim();
	        String detailAddress = etDetailAddress.getText().toString().trim();
	        String tvLabelString = tvLabel.getText().toString();
	        int checkId = rgSex.getCheckedRadioButtonId();
	        String sex = "";
	        if (checkId == R.id.rb_man) {
	            sex = "男性";
	        } else {
	            sex = "女性";
	        }
	        receiptAddressBean = new ReceiptAddressBean(MyApplication.userId, name,
	                sex, phone, otherPhone, "南宫燚滨", detailAddress, tvLabelString, 0);
	        receiptAddressDao.create(receiptAddressBean);
	        //结束当前的activity
	        finish();
	    }
	
	    private boolean checkData() {
	        String name = etName.getText().toString().trim();
	        if (TextUtils.isEmpty(name)) {
	            Toast.makeText(this, "请填写联系人", Toast.LENGTH_SHORT).show();
	            return false;
	        }
	        String phone = etPhone.getText().toString().trim();
	        if (TextUtils.isEmpty(phone)) {
	            Toast.makeText(this, "请填写手机号码", Toast.LENGTH_SHORT).show();
	            return false;
	        }
	        if (!SMSUtil.isMobileNO(phone)) {
	            Toast.makeText(this, "请填写合法的手机号", Toast.LENGTH_SHORT).show();
	            return false;
	        }
	//        String receiptAddress = tvReceiptAddress.getText().toString().trim();
	//        if (SMSUtil.isMobileNO(receiptAddress)){
	//            Toast.makeText(this, "请选择收货地址", Toast.LENGTH_SHORT).show();
	//            return false;
	//        }
	        String address = etDetailAddress.getText().toString().trim();
	        if (SMSUtil.isMobileNO(address)) {
	            Toast.makeText(this, "请填写详细地址", Toast.LENGTH_SHORT).show();
	            return false;
	        }
	        int checkedRadioButtonId = rgSex.getCheckedRadioButtonId();
	        if (checkedRadioButtonId != R.id.rb_man && checkedRadioButtonId != R.id.rb_women) {
	            //2个不相等,则说明没有选中任意一个
	            Toast.makeText(this, "请选择性别", Toast.LENGTH_SHORT).show();
	            return false;
	        }
	        String tvLabelString = tvLabel.getText().toString();
	        if (SMSUtil.isMobileNO(tvLabelString)) {
	            Toast.makeText(this, "请输入标签信息", Toast.LENGTH_SHORT).show();
	            return false;
	        }
	        return true;
	    }
	
	    private void showLabelDialog() {
	        AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setTitle("请选择标签?");
	        builder.setItems(addressLabels, new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                //标签内容在文本中显示
	                tvLabel.setText(addressLabels[which]);
	                //给标签控件设置背景颜色
	                tvLabel.setBackgroundColor(bgLabels[which]);
	            }
	        });
	        builder.show();
	    }
	}


## 6 定位以及周边 ##
>通过高德地图获取定位位置,以及周边地址

    
	public class OrderDetailActivity extends Activity implements Observer {
	    @BindView(R.id.iv_order_detail_back)
	    ImageView ivOrderDetailBack;
	    @BindView(R.id.tv_seller_name)
	    TextView tvSellerName;
	    @BindView(R.id.tv_order_detail_time)
	    TextView tvOrderDetailTime;
	    @BindView(R.id.map)
	    MapView map;
	    @BindView(R.id.ll_order_detail_type_container)
	    LinearLayout llOrderDetailTypeContainer;
	    @BindView(R.id.ll_order_detail_type_point_container)
	    LinearLayout llOrderDetailTypePointContainer;
	    private String orderId;
	    private String type;
	    private int index = -1;
	    AMap aMap;
	    private LatLng latLngBuyter;//设置买家唯独坐标对象
	    private LatLng latLngSeller;
	    private Marker riderMarker;
	    private LatLng riderLatLng;
	    private LatLng currentPos;
	    private ArrayList<LatLng> riderPosList;
	
	    @Override
	    protected void onCreate(@Nullable Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        orderId = getIntent().getStringExtra("orderId");
	        type = getIntent().getStringExtra("type");
	        setContentView(R.layout.activity_order_detail);
	        ButterKnife.bind(this);
	        map.onCreate(savedInstanceState);
	        //初始化地图控制器对象
	        if (aMap == null) {
	            aMap = map.getMap();
	        }
	        //在此界面中注册观察者过程
	        //注册广播接受者
	        OrderObservable.getInstance().addObserver(this);
	        getIndex(type);
	        if (index != -1) {
	            //根据index的索引位置,更新文字和颜色
	            changeUI();
	        }
	    }
	
	    private void changeUI() {
	        //将所有的文字和图片都设置为黑色和灰色
	        for (int i = 0; i < 4; i++) {
	            ((TextView) llOrderDetailTypeContainer.getChildAt(i)).setTextColor(Color.BLACK);
	            ((ImageView) llOrderDetailTypePointContainer.getChildAt(i)).setImageResource(R.mipmap.order_time_node_normal);
	        }
	        ((TextView) llOrderDetailTypeContainer.getChildAt(index)).setTextColor(Color.BLUE);
	        ((ImageView) llOrderDetailTypePointContainer.getChildAt(index)).setImageResource(R.mipmap.order_time_node_disabled);
	    }
	
	    /**
	     * @param type 根据前一个页面传递过来的type决定需要变蓝点和文字的索引位置
	     */
	    private void getIndex(String type) {
	        switch (type) {
	            case OrderObservable.ORDERTYPE_SUBMIT:
	                index = 0;
	                break;
	            case OrderObservable.ORDERTYPE_RECEIVEORDER:
	                index = 1;
	                break;
	            case OrderObservable.ORDERTYPE_DISTRIBUTION:
	                index = 2;
	                break;
	            case OrderObservable.ORDERTYPE_SERVED:
	                index = 3;
	                break;
	        }
	    }
	
	    @Override
	    public void update(Observable o, Object arg) {
	        HashMap<String, String> hashMap = (HashMap<String, String>) arg;
	        String orderInfo = hashMap.get("orderInfo");
	        String type = hashMap.get("type");
	        //通过type获取新的选中蓝色条目的索引
	        getIndex(type);
	        if (index != -1) {
	            changeUI();
	        }
	        switch (type) {
	            case OrderObservable.ORDERTYPE_RECEIVEORDER:
	                //商家已接单,需要显示地图
	                initMap();
	                break;
	            case OrderObservable.ORDERTYPE_DISTRIBUTION://商家已经接单
	                initRider(hashMap);//骑手
	                break;
	            case OrderObservable.ORDERTYPE_DISTRIBUTION_RIDER_TAKE_MEAL://骑手取餐
	            case OrderObservable.ORDERTYPE_DISTRIBUTION_RIDER_GIVE_MEAL://骑手送餐
	                changeRider(hashMap, type);
	                break;
	        }
	    }
	
	    /**
	     * @param hashMap
	     * @param type    骑手取餐和送餐行走轨迹绘制方法
	     */
	    private void changeRider(HashMap<String, String> hashMap, String type) {
	        //获取骑手在行走过程中经纬度坐标
	        String lat = hashMap.get("lat");
	        String lng = hashMap.get("lng");
	        double dLat = Double.parseDouble(lat);
	        double dLng = Double.parseDouble(lng);
	        currentPos = new LatLng(dLat, dLng);
	        //记录目前骑手的经纬度坐标在一个有序的集合中
	        riderPosList.add(currentPos);
	        //时刻保证骑手在地图的中心位置
	        //设置骑手的所在位置
	        riderMarker.setPosition(currentPos);
	        //地图定位焦点
	        aMap.moveCamera(CameraUpdateFactory.changeLatLng(currentPos));
	        String info = "";
	        DecimalFormat format = new DecimalFormat(".00");
	        switch (type) {
	            case OrderObservable.ORDERTYPE_DISTRIBUTION_RIDER_TAKE_MEAL://取餐
	                //取餐,距离卖家的距离
	                //calculateLineDistance骑手现在所在的经纬度,商家现在所在的经纬度
	                float ds = AMapUtils.calculateLineDistance(currentPos, latLngSeller);
	                info = "距离商家" + format.format(ds) + "米";
	                break;
	            case OrderObservable.ORDERTYPE_DISTRIBUTION_RIDER_GIVE_MEAL:
	                //送餐 ,距离买家的距离
	                //参数一  骑手当前的所在位置
	                //参数二  买家所在的位置
	                //计算得出2个经纬度的直线距离db
	                float db = AMapUtils.calculateLineDistance(currentPos, latLngBuyter);
	                //给db保留小数点后2为显示
	                info = "距离买家" + format.format(db) + "米";
	                break;
	        }
	        //要将计算出来的距离放置在骑手描述文本上
	        riderMarker.setSnippet(info);
	        //将设置的信息在界面中显示
	        riderMarker.showInfoWindow();
	        //参数一当前所处位置
	        //参数二 历史的点,前一个点
	        changeRider(currentPos, riderPosList.get(riderPosList.size() - 2));
	    }
	
	    private void changeRider(LatLng currentPos, LatLng pos) {
	        //绘制线绿色,宽度为2像素
	        aMap.addPolyline(new PolylineOptions().add(pos, currentPos).width(2).color(Color.GREEN));
	    }
	
	    private void initRider(HashMap<String, String> hashMap) {
	        riderPosList = new ArrayList<>();
	        //将接单的快递小哥经纬获取,并且将显示在地图上
	        String lat = hashMap.get("lat");
	        String lng = hashMap.get("lng");
	        double dLat = Double.parseDouble(lat);
	        double dLng = Double.parseDouble(lng);
	        riderLatLng = new LatLng(dLat, dLng);
	        //修改地图的缩放级别
	        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
	        //创建一个在地图上需要显示的点的对象
	        riderMarker = aMap.addMarker(new MarkerOptions().position(riderLatLng));
	        //给要显示的点准备图片
	        BitmapDescriptor descriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.order_rider_icon));
	        //将图片添加在点上
	        riderMarker.setIcon(descriptor);
	        //骑手接单描述内容
	        riderMarker.setSnippet("骑手已接单");
	        //显示骑手
	        riderMarker.showInfoWindow();
	        //将快递小哥接单的位置显示在地图的中心
	        aMap.moveCamera(CameraUpdateFactory.changeLatLng(riderLatLng));
	        //在集合中添加骑手的开始点
	        riderPosList.add(riderLatLng);
	    }
	
	    private void initMap() {
	        //显示地图
	        map.setVisibility(View.VISIBLE);
	        //修改地图的缩放级别
	        aMap.moveCamera(CameraUpdateFactory.zoomTo(13));
	        //显示买卖双方,在地图上展示经纬度坐标
	        //添加买家marker
	        latLngBuyter = new LatLng(40.100519, 116.365828);
	        //给买家在地图上显示的时候提供一张图片
	        MarkerOptions markerOptions = new MarkerOptions();
	        Marker marker = aMap.addMarker(markerOptions.position(latLngBuyter));
	        //解析资源文件中的图片资源
	        BitmapDescriptor descriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.order_buyer_icon));
	        //将图片资源放置在地图点的创建对象上
	        marker.setIcon(descriptor);
	        //将地图的中心指向某一个经纬坐标点,指定地图的中心点为买家
	        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLngBuyter));
	        //添加卖家marker
	        latLngSeller = new LatLng(40.060244, 116.343513);
	        //创建一个在地图上需要现实的额点的对象
	        Marker marker1 = aMap.addMarker(new MarkerOptions().position(latLngSeller));
	        //给要显示的点准备图片
	        BitmapDescriptor descriptor1 = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.order_seller_icon));
	        //将图片添加在点上
	        marker1.setIcon(descriptor1);
	    }
	
	    @Override
	    protected void onDestroy() {
	        //注销观察者
	        OrderObservable.getInstance().deleteObserver(this);
	        map.onDestroy();
	        super.onDestroy();
	    }
	
	    @Override
	    protected void onResume() {
	        super.onResume();
	        //在activity执行onResume时执行mapview.onResume(),重新绘制加载地图
	        map.onResume();
	    }
	
	    @Override
	    protected void onPause() {
	        super.onPause();
	        //在activity执行onPause时执行mapview.onPause(),暂停地图的绘制
	        map.onPause();
	    }
	
	    @Override
	    protected void onSaveInstanceState(Bundle outState) {
	        super.onSaveInstanceState(outState);
	        //在activity执行onSaveInstanceState时执行mapview.onSaveInstanceState(outState),保存地图当前的状态
	        map.onSaveInstanceState(outState);
	    }
	}

## 7 周边地址列表 ##

    
	public class OrderDetailActivity extends Activity implements Observer {
	    @BindView(R.id.iv_order_detail_back)
	    ImageView ivOrderDetailBack;
	    @BindView(R.id.tv_seller_name)
	    TextView tvSellerName;
	    @BindView(R.id.tv_order_detail_time)
	    TextView tvOrderDetailTime;
	    @BindView(R.id.map)
	    MapView map;
	    @BindView(R.id.ll_order_detail_type_container)
	    LinearLayout llOrderDetailTypeContainer;
	    @BindView(R.id.ll_order_detail_type_point_container)
	    LinearLayout llOrderDetailTypePointContainer;
	    private String orderId;
	    private String type;
	    private int index = -1;
	    AMap aMap;
	    private LatLng latLngBuyter;//设置买家唯独坐标对象
	    private LatLng latLngSeller;
	    private Marker riderMarker;
	    private LatLng riderLatLng;
	    private LatLng currentPos;
	    private ArrayList<LatLng> riderPosList;
	
	    @Override
	    protected void onCreate(@Nullable Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        orderId = getIntent().getStringExtra("orderId");
	        type = getIntent().getStringExtra("type");
	        setContentView(R.layout.activity_order_detail);
	        ButterKnife.bind(this);
	        map.onCreate(savedInstanceState);
	        //初始化地图控制器对象
	        if (aMap == null) {
	            aMap = map.getMap();
	        }
	        //在此界面中注册观察者过程
	        //注册广播接受者
	        OrderObservable.getInstance().addObserver(this);
	        getIndex(type);
	        if (index != -1) {
	            //根据index的索引位置,更新文字和颜色
	            changeUI();
	        }
	    }
	
	    private void changeUI() {
	        //将所有的文字和图片都设置为黑色和灰色
	        for (int i = 0; i < 4; i++) {
	            ((TextView) llOrderDetailTypeContainer.getChildAt(i)).setTextColor(Color.BLACK);
	            ((ImageView) llOrderDetailTypePointContainer.getChildAt(i)).setImageResource(R.mipmap.order_time_node_normal);
	        }
	        ((TextView) llOrderDetailTypeContainer.getChildAt(index)).setTextColor(Color.BLUE);
	        ((ImageView) llOrderDetailTypePointContainer.getChildAt(index)).setImageResource(R.mipmap.order_time_node_disabled);
	    }
	
	    /**
	     * @param type 根据前一个页面传递过来的type决定需要变蓝点和文字的索引位置
	     */
	    private void getIndex(String type) {
	        switch (type) {
	            case OrderObservable.ORDERTYPE_SUBMIT:
	                index = 0;
	                break;
	            case OrderObservable.ORDERTYPE_RECEIVEORDER:
	                index = 1;
	                break;
	            case OrderObservable.ORDERTYPE_DISTRIBUTION:
	                index = 2;
	                break;
	            case OrderObservable.ORDERTYPE_SERVED:
	                index = 3;
	                break;
	        }
	    }
	
	    @Override
	    public void update(Observable o, Object arg) {
	        HashMap<String, String> hashMap = (HashMap<String, String>) arg;
	        String orderInfo = hashMap.get("orderInfo");
	        String type = hashMap.get("type");
	        //通过type获取新的选中蓝色条目的索引
	        getIndex(type);
	        if (index != -1) {
	            changeUI();
	        }
	        switch (type) {
	            case OrderObservable.ORDERTYPE_RECEIVEORDER:
	                //商家已接单,需要显示地图
	                initMap();
	                break;
	            case OrderObservable.ORDERTYPE_DISTRIBUTION://商家已经接单
	                initRider(hashMap);//骑手
	                break;
	            case OrderObservable.ORDERTYPE_DISTRIBUTION_RIDER_TAKE_MEAL://骑手取餐
	            case OrderObservable.ORDERTYPE_DISTRIBUTION_RIDER_GIVE_MEAL://骑手送餐
	                changeRider(hashMap, type);
	                break;
	        }
	    }
	
	    /**
	     * @param hashMap
	     * @param type    骑手取餐和送餐行走轨迹绘制方法
	     */
	    private void changeRider(HashMap<String, String> hashMap, String type) {
	        //获取骑手在行走过程中经纬度坐标
	        String lat = hashMap.get("lat");
	        String lng = hashMap.get("lng");
	        double dLat = Double.parseDouble(lat);
	        double dLng = Double.parseDouble(lng);
	        currentPos = new LatLng(dLat, dLng);
	        //记录目前骑手的经纬度坐标在一个有序的集合中
	        riderPosList.add(currentPos);
	        //时刻保证骑手在地图的中心位置
	        //设置骑手的所在位置
	        riderMarker.setPosition(currentPos);
	        //地图定位焦点
	        aMap.moveCamera(CameraUpdateFactory.changeLatLng(currentPos));
	        String info = "";
	        DecimalFormat format = new DecimalFormat(".00");
	        switch (type) {
	            case OrderObservable.ORDERTYPE_DISTRIBUTION_RIDER_TAKE_MEAL://取餐
	                //取餐,距离卖家的距离
	                //calculateLineDistance骑手现在所在的经纬度,商家现在所在的经纬度
	                float ds = AMapUtils.calculateLineDistance(currentPos, latLngSeller);
	                info = "距离商家" + format.format(ds) + "米";
	                break;
	            case OrderObservable.ORDERTYPE_DISTRIBUTION_RIDER_GIVE_MEAL:
	                //送餐 ,距离买家的距离
	                //参数一  骑手当前的所在位置
	                //参数二  买家所在的位置
	                //计算得出2个经纬度的直线距离db
	                float db = AMapUtils.calculateLineDistance(currentPos, latLngBuyter);
	                //给db保留小数点后2为显示
	                info = "距离买家" + format.format(db) + "米";
	                break;
	        }
	        //要将计算出来的距离放置在骑手描述文本上
	        riderMarker.setSnippet(info);
	        //将设置的信息在界面中显示
	        riderMarker.showInfoWindow();
	        //参数一当前所处位置
	        //参数二 历史的点,前一个点
	        changeRider(currentPos, riderPosList.get(riderPosList.size() - 2));
	    }
	
	    private void changeRider(LatLng currentPos, LatLng pos) {
	        //绘制线绿色,宽度为2像素
	        aMap.addPolyline(new PolylineOptions().add(pos, currentPos).width(2).color(Color.GREEN));
	    }
	
	    private void initRider(HashMap<String, String> hashMap) {
	        riderPosList = new ArrayList<>();
	        //将接单的快递小哥经纬获取,并且将显示在地图上
	        String lat = hashMap.get("lat");
	        String lng = hashMap.get("lng");
	        double dLat = Double.parseDouble(lat);
	        double dLng = Double.parseDouble(lng);
	        riderLatLng = new LatLng(dLat, dLng);
	        //修改地图的缩放级别
	        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
	        //创建一个在地图上需要显示的点的对象
	        riderMarker = aMap.addMarker(new MarkerOptions().position(riderLatLng));
	        //给要显示的点准备图片
	        BitmapDescriptor descriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.order_rider_icon));
	        //将图片添加在点上
	        riderMarker.setIcon(descriptor);
	        //骑手接单描述内容
	        riderMarker.setSnippet("骑手已接单");
	        //显示骑手
	        riderMarker.showInfoWindow();
	        //将快递小哥接单的位置显示在地图的中心
	        aMap.moveCamera(CameraUpdateFactory.changeLatLng(riderLatLng));
	        //在集合中添加骑手的开始点
	        riderPosList.add(riderLatLng);
	    }
	
	    private void initMap() {
	        //显示地图
	        map.setVisibility(View.VISIBLE);
	        //修改地图的缩放级别
	        aMap.moveCamera(CameraUpdateFactory.zoomTo(13));
	        //显示买卖双方,在地图上展示经纬度坐标
	        //添加买家marker
	        latLngBuyter = new LatLng(40.100519, 116.365828);
	        //给买家在地图上显示的时候提供一张图片
	        MarkerOptions markerOptions = new MarkerOptions();
	        Marker marker = aMap.addMarker(markerOptions.position(latLngBuyter));
	        //解析资源文件中的图片资源
	        BitmapDescriptor descriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.order_buyer_icon));
	        //将图片资源放置在地图点的创建对象上
	        marker.setIcon(descriptor);
	        //将地图的中心指向某一个经纬坐标点,指定地图的中心点为买家
	        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLngBuyter));
	        //添加卖家marker
	        latLngSeller = new LatLng(40.060244, 116.343513);
	        //创建一个在地图上需要现实的额点的对象
	        Marker marker1 = aMap.addMarker(new MarkerOptions().position(latLngSeller));
	        //给要显示的点准备图片
	        BitmapDescriptor descriptor1 = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.order_seller_icon));
	        //将图片添加在点上
	        marker1.setIcon(descriptor1);
	    }
	
	    @Override
	    protected void onDestroy() {
	        //注销观察者
	        OrderObservable.getInstance().deleteObserver(this);
	        map.onDestroy();
	        super.onDestroy();
	    }
	
	    @Override
	    protected void onResume() {
	        super.onResume();
	        //在activity执行onResume时执行mapview.onResume(),重新绘制加载地图
	        map.onResume();
	    }
	
	    @Override
	    protected void onPause() {
	        super.onPause();
	        //在activity执行onPause时执行mapview.onPause(),暂停地图的绘制
	        map.onPause();
	    }
	
	    @Override
	    protected void onSaveInstanceState(Bundle outState) {
	        super.onSaveInstanceState(outState);
	        //在activity执行onSaveInstanceState时执行mapview.onSaveInstanceState(outState),保存地图当前的状态
	        map.onSaveInstanceState(outState);
	    }
	}

# 支付模块笔记 # 
## 跳转至订单确认模块 ##

    
	public class ConfirmOrderActivity extends Activity {
	    @BindView(R.id.ib_back)
	    ImageButton ibBack;
	    @BindView(R.id.tv_login)
	    TextView tvLogin;
	    @BindView(R.id.iv_location)
	    ImageView ivLocation;
	    @BindView(R.id.tv_hint_select_receipt_address)
	    TextView tvHintSelectReceiptAddress;
	    @BindView(R.id.tv_name)
	    TextView tvName;
	    @BindView(R.id.tv_sex)
	    TextView tvSex;
	    @BindView(R.id.tv_phone)
	    TextView tvPhone;
	    @BindView(R.id.tv_label)
	    TextView tvLabel;
	    @BindView(R.id.tv_address)
	    TextView tvAddress;
	    @BindView(R.id.ll_receipt_address)
	    LinearLayout llReceiptAddress;
	    @BindView(R.id.iv_arrow)
	    ImageView ivArrow;
	    @BindView(R.id.rl_location)
	    RelativeLayout rlLocation;
	    //    @BindView(R.id.iv_arrow)
	//    ImageView ivArrow;
	//    @BindView(R.id.iv_arrow)
	//    ImageView ivArrow;
	//    @BindView(R.id.iv_arrow)
	//    ImageView ivArrow;
	    @BindView(R.id.iv_icon)
	    ImageView ivIcon;
	    @BindView(R.id.tv_seller_name)
	    TextView tvSellerName;
	    @BindView(R.id.ll_select_goods)
	    LinearLayout llSelectGoods;
	    @BindView(R.id.tv_deliveryFee)
	    TextView tvDeliveryFee;
	    @BindView(R.id.tv_CountPrice)
	    TextView tvCountPrice;
	    @BindView(R.id.tvSubmit)
	    TextView tvSubmit;
	    //    @BindView(R.id.iv_icon)
	//    ImageView ivIcon;
	    private ReceiptAddressDao receiptAddressDao;
	    private String[] addressLabels;
	    private int[] bgLabels;
	
	    @Override
	    protected void onCreate(@Nullable Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_confirm_order);
	        ButterKnife.bind(this);
	        addressLabels = new String[]{"家", "公司", "学校"};
	        bgLabels = new int[]{Color.parseColor("#fc7251"), Color.parseColor("#468ade"), Color.parseColor("#02c14b")};
	        receiptAddressDao = new ReceiptAddressDao(this);
	        //获取前一个页面在集合中存储的商品
	        ArrayList<GoodsInfo> shopcartList = (ArrayList<GoodsInfo>) getIntent().getSerializableExtra("shopcartList");
	        //运费
	        float deliveryFee = getIntent().getFloatExtra("deliveryFee", 0.0f);
	        //运费展示
	        tvDeliveryFee.setText(CountPriceFormater.format(deliveryFee));
	        //llSelectGoods容器中添加购买商品的列表
	        //清空线性布局中所有的控件
	        llSelectGoods.removeAllViews();
	        float totalPrice = 0.0f;
	        for (int i = 0; i < shopcartList.size(); i++) {
	            GoodsInfo goodsInfo = shopcartList.get(i);
	            //购买商品数量
	            int count = goodsInfo.getCount();
	            //购买商品金额
	            float price = count * goodsInfo.getNewPrice();
	            totalPrice += price;
	            //单个条目布局文件转换成view对象
	            View view = View.inflate(this, R.layout.item_confirm_order_goods, null);
	            TextView tvName = view.findViewById(R.id.tv_name);
	            TextView tvCount = view.findViewById(R.id.tv_count);
	            TextView tvPrice = view.findViewById(R.id.tv_price);
	            tvName.setText(goodsInfo.getName());
	            tvCount.setText(count + "");
	            tvPrice.setText(CountPriceFormater.format(price));
	            llSelectGoods.addView(view);
	        }
	        totalPrice += deliveryFee;
	        tvCountPrice.setText("待支付:" + CountPriceFormater.format(totalPrice));
	    }
	
	    @Override
	    protected void onResume() {
	        //查询登录用户的选中送货地址
	        ReceiptAddressBean receiptAddressBean = receiptAddressDao.queryUserSelectAddress(MyApplication.userId);
	        if (receiptAddressBean != null) {
	            //设置名称
	            tvName.setText(receiptAddressBean.getName());
	            tvSex.setText(receiptAddressBean.getSex());
	            //有2个号码可以显示
	            if (!TextUtils.isEmpty(receiptAddressBean.getPhone()) && !TextUtils.isEmpty(receiptAddressBean.getPhoneOther())) {
	                tvPhone.setText(receiptAddressBean.getPhone() + "," + receiptAddressBean.getPhoneOther());
	            }
	            //有一个号码可以显示
	            if (!TextUtils.isEmpty(receiptAddressBean.getPhone()) && TextUtils.isEmpty(receiptAddressBean.getPhoneOther())) {
	                tvPhone.setText(receiptAddressBean.getPhone());
	            }
	            //显示地址是公司,还是学校,还是家
	            tvLabel.setText(receiptAddressBean.getLabel());
	            //获取label后需要获取数组的索引位置,通过索引位置获取tvLabel控件的背景色
	            int index = getIndex(receiptAddressBean.getLabel());
	            tvLabel.setBackgroundColor(bgLabels[index]);
	            //显示初略地址和详细地址的合并结果
	            tvAddress.setText(receiptAddressBean.getReceiptAddress() + receiptAddressBean.getDetailAddress());
	        }
	        super.onResume();
	    }
	
	    private int getIndex(String label) {
	        int index = -1;
	        for (int i = 0; i < addressLabels.length; i++) {
	            if (addressLabels[i].equals(label)) {
	                index = i;
	                break;
	            }
	        }
	        return index;
	    }
	
	    @OnClick({R.id.rl_location, R.id.tvSubmit})
	    public void onViewClicked(View view) {
	        switch (view.getId()) {
	            case R.id.rl_location:
	                Intent intent = new Intent(this, AddressListActivity.class);
	                startActivity(intent);
	                break;
	            case R.id.tvSubmit:
	                Intent intent1 = new Intent(this, PayOnlineActivity.class);
	                startActivity(intent1);
	                break;
	        }
	    }
	}

## 跳转至订单支付模块 ##

    
	public class PayOnlineActivity extends Activity {
	    private static final String TAG = "PayOnlineActivity";
	    public static final int SDK_PAY_FLAG = 100;
	    //支付宝支付业务  入参APPID
	    public static final String APPID = "2016092800612254";
	    //商家私钥,pkcs8格式
	    public static final String RSA_PRIVATE = "MIICeQIBADANBgkqhkiG9w0BAQEFAASCAmMwggJfAgEAAoGBAMMBchkEf8EE3PRYvMNgdFbLy5Or4YDNn2rbaRCpdiaZ2MZa4b8yiLHGVbd6H/qAdCkyhZdpiJsjt2+5gxJ/fp9ASAi05P5XzzRyuLoIblJ+s1z+uPBFZ5wbEpF3HKRi1bxoEEvgkUbJt3fCS5IbewzsitKXLwIbMg3Xs98FhVhtAgMBAAECgYEAvsuZUTT/smEJtuFjv12ONqYrRw7zEk0dXzXR9IV5nocEe/7LxYCyEg5WK7CHvVcVUIGoB98v02r8vYmq3txzi7LgxCz1iqV3qvZBp4M8746j4WPzzxlpLCJhkGLZwdVHRJYDimzbrR95fzB1pb5PjigTgfqNLbTPWiSa8FlBhNkCQQDvQE7J491rLmC7dI9lO9a8ehkJOEVLLgGBjjeOZKTvI+UMkR2OgN6m8Eel7WcyiWDg3/44A5F13rpRDaRFumITAkEA0KgzO1kvszt9j3e5pji9heSYQUX7yVlegg0KIahaWsD7HbneCDJNX8+uW7tRNo0HsDgOX3n6LwTO4gv5yMSrfwJBAKHW8nUHOsRB/xVO56EnNT8RqNXfxp/wYvxsY5Bi7F8H/OzfTQsUiYiLiSvox6Ib4/i6DJF/eGovXAFwJOs2BRkCQQC9qmcHZ4aIcWcs5jOG+MSt1KDhlG/PEsFpjdjLx3B6VjjtSGqfILpcgTeIgrjL0V5PvGS0PPhBjSlRKHmhGcSZAkEAg/P8vnJyP4PWxBBQRDG2kBfJASpgtngV0nB45NCFYOO4WPFFgVk+HoCQ8dz/0nBUyPO/3dVDfj/Wnj1W3wjIvw==";
	    @BindView(R.id.ib_back)
	    ImageButton ibBack;
	    @BindView(R.id.tv_residualTime)
	    TextView tvResidualTime;
	    @BindView(R.id.tv_order_name)
	    TextView tvOrderName;
	    @BindView(R.id.tv)
	    TextView tv;
	    @BindView(R.id.tv_order_detail)
	    TextView tvOrderDetail;
	    @BindView(R.id.iv_triangle)
	    ImageView ivTriangle;
	    @BindView(R.id.ll_order_toggle)
	    RelativeLayout llOrderToggle;
	    @BindView(R.id.tv_receipt_connect_info)
	    TextView tvReceiptConnectInfo;
	    @BindView(R.id.tv_receipt_address_info)
	    TextView tvReceiptAddressInfo;
	    @BindView(R.id.ll_goods)
	    LinearLayout llGoods;
	    @BindView(R.id.ll_order_detail)
	    LinearLayout llOrderDetail;
	    @BindView(R.id.tv_pay_money)
	    TextView tvPayMoney;
	    @BindView(R.id.iv_pay_alipay)
	    ImageView ivPayAlipay;
	    @BindView(R.id.cb_pay_alipay)
	    CheckBox cbPayAlipay;
	    @BindView(R.id.tv_selector_other_payment)
	    TextView tvSelectorOtherPayment;
	    @BindView(R.id.ll_hint_info)
	    LinearLayout llHintInfo;
	    @BindView(R.id.iv_pay_wechat)
	    ImageView ivPayWechat;
	    @BindView(R.id.cb_pay_wechat)
	    CheckBox cbPayWechat;
	    @BindView(R.id.iv_pay_qq)
	    ImageView ivPayQq;
	    @BindView(R.id.cb_pay_qq)
	    CheckBox cbPayQq;
	    @BindView(R.id.iv_pay_fenqile)
	    ImageView ivPayFenqile;
	    @BindView(R.id.cb_pay_fenqile)
	    CheckBox cbPayFenqile;
	    @BindView(R.id.ll_other_payment)
	    LinearLayout llOtherPayment;
	    @BindView(R.id.bt_confirm_pay)
	    Button btConfirmPay;
	    private Handler mHandler = new Handler() {
	        public void handleMessage(Message msg) {
	            switch (msg.what) {
	                case SDK_PAY_FLAG: {
	                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
	                    /**
	                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
	                     */
	                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
	                    String resultStatus = payResult.getResultStatus();
	                    Log.e(TAG, "handleMessage: " + resultStatus);
	                    // 判断resultStatus 为9000则代表支付成功
	                    if (TextUtils.equals(resultStatus, "9000")) {
	                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
	                        Toast.makeText(PayOnlineActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
	                        //给公司的服务器发送请求,告知服务器客户端是支付成功了,服务器你也改一下支付状态吧
	                        //好的,我的状态改过了,你给用户显示这个支付成功的结果吧
	                    } else {
	                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
	                        Toast.makeText(PayOnlineActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
	                    }
	                    break;
	                }
	            }
	        }
	
	        ;
	    };
	
	    @Override
	    protected void onCreate(@Nullable Bundle savedInstanceState) {
	        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_online_payment);
	        ButterKnife.bind(this);
	    }
	
	    @OnClick(R.id.bt_confirm_pay)
	    public void onViewClicked() {
	        //调用支付宝进行支付
	        pay();
	    }
	
	    /**
	     * 支付宝支付业务
	     */
	    public void pay() {
	        /**
	         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
	         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
	         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
	         *
	         * orderInfo的获取必须来自服务端；
	         */
	        //维护需要上传的参数键值对
	        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID);
	        //将键值对转换成key ==value&key =value型式
	        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
	        //生成签名过程
	        String sign = OrderInfoUtil2_0.getSign(params, RSA_PRIVATE);
	        //订单加签过程
	        final String orderInfo = orderParam + "&" + sign;
	        Log.e(TAG, "pay: " + orderInfo);
	        //客户端代码从此开始
	        Runnable payRunnable = new Runnable() {
	
	            @Override
	            public void run() {
	                //子线程中执行的代码
	                //创建一个支付的任务
	                PayTask alipay = new PayTask(PayOnlineActivity.this);
	                //触发一个支付的请求网路
	                Map<String, String> result = alipay.payV2(orderInfo, true);
	                Log.e(TAG, result.toString());
	                Message msg = new Message();
	                msg.what = SDK_PAY_FLAG;
	                msg.obj = result;
	                mHandler.sendMessage(msg);
	            }
	        };
	        Thread payThread = new Thread(payRunnable);
	        payThread.start();
	    }
	}



效果图:

![](https://github.com/nangongyibin/Android_TakeOut/blob/master/aa.gif?raw=true)