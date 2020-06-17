package com.ngyb.takeout.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ngyb.mvpbase.BaseMvpActivity;
import com.ngyb.takeout.R;
import com.ngyb.takeout.app.MyApplication;
import com.ngyb.takeout.constant.Constant;
import com.ngyb.takeout.contract.AddAddressContract;
import com.ngyb.takeout.dao.ReceiptAddressDao;
import com.ngyb.takeout.dao.bean.ReceiptAddressBean;
import com.ngyb.takeout.presenter.AddAddressPresenter;
import com.ngyb.utils.PhoneUtils;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/6/7 21:00
 */
public class AddAddressActivity extends BaseMvpActivity<AddAddressPresenter> implements AddAddressContract.View, View.OnFocusChangeListener, View.OnClickListener {

    private ImageButton ibBack;
    private TextView tvTitle;
    private ImageButton ibDelete;
    private TextView tvName;
    private EditText etName;
    private RadioButton rbMan;
    private RadioButton rbWomen;
    private RadioGroup rgSex;
    private EditText etPhone;
    private ImageButton ibDeletePhone;
    private ImageButton ibDeletePhoneOther;
    private RelativeLayout rlPhoneOther;
    private TextView tvReceiptAddress;
    private EditText etDetailAddress;
    private TextView tvLabel;
    private ImageView ibSelectLabel;
    private Button btOk;
    private ImageButton ibAddPhoneOther;
    private EditText etPhoneOther;
    private String[] addressLabels;
    private int[] bgLabels;
    private ReceiptAddressDao receiptAddressDao;
    private ReceiptAddressBean receiptAddressBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_address;
    }

    @Override
    public void init() {
        initView();
        initData();
        initClass();
        initIntent();
        initListener();
    }

    private void initIntent() {
        //通过编辑按钮跳转到此界面传递过来的地址对象
        receiptAddressBean = (ReceiptAddressBean) getIntent().getSerializableExtra(Constant.RECEIPTADDRESS);
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

    private void initListener() {
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                phoneAfter(etPhone, ibDeletePhone);
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
                phoneAfter(etPhoneOther, ibDeletePhoneOther);
            }
        });
        //对EditText中的焦点是否存在监听
        etPhone.setOnFocusChangeListener(this);
        etPhoneOther.setOnFocusChangeListener(this);
        ibDelete.setOnClickListener(this);
        ibAddPhoneOther.setOnClickListener(this);
        ibSelectLabel.setOnClickListener(this);
        btOk.setOnClickListener(this);
    }

    private void phoneAfter(EditText et, ImageButton ib) {
        String phone = et.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            ib.setVisibility(View.GONE);
        } else {
            ib.setVisibility(View.VISIBLE);
        }
    }

    private void initClass() {
        receiptAddressDao = new ReceiptAddressDao(this);

    }

    private void initData() {
        addressLabels = new String[]{"家", "公司", "学校"};
        bgLabels = new int[]{Color.parseColor("#fc7251"), Color.parseColor("#468ade"), Color.parseColor("#02c14b")};
    }

    private void initView() {
        ibBack = findViewById(R.id.ib_back);
        tvTitle = findViewById(R.id.tv_title);
        ibDelete = findViewById(R.id.ib_delete);
        tvName = findViewById(R.id.tv_name);
        etName = findViewById(R.id.et_name);
        rbMan = findViewById(R.id.rb_man);
        rbWomen = findViewById(R.id.rb_women);
        rgSex = findViewById(R.id.rg_sex);
        etPhone = findViewById(R.id.et_phone);
        ibDeletePhone = findViewById(R.id.ib_delete_phone);
        ibAddPhoneOther = findViewById(R.id.ib_add_phone_other);
        etPhoneOther = findViewById(R.id.et_phone_other);
        ibDeletePhoneOther = findViewById(R.id.ib_delete_phone_other);
        rlPhoneOther = findViewById(R.id.rl_phone_other);
        tvReceiptAddress = findViewById(R.id.tv_receipt_address);
        etDetailAddress = findViewById(R.id.et_detail_address);
        tvLabel = findViewById(R.id.tv_label);
        ibSelectLabel = findViewById(R.id.ib_select_label);
        btOk = findViewById(R.id.bt_ok);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        //Edittext焦点发生变化时调用方法，v即使edittext对象本身，hasFocus对象是否有焦点。
        if (v.getId() == R.id.et_phone) {
            phoneFocus(hasFocus, etPhone, ibDeletePhone);
        } else {
            phoneFocus(hasFocus, etPhoneOther, ibDeletePhoneOther);
        }
    }

    private void phoneFocus(boolean hasFocus, EditText et, ImageButton ib) {
        String phone = et.getText().toString().trim();
        if (hasFocus && !TextUtils.isEmpty(phone)) {
            //既有文本，又有焦点
            ib.setVisibility(View.VISIBLE);
        } else {
            ib.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_delete:
                if (receiptAddressBean != null) {
                    //删除receiptAddressBean对象，从数据库中删除receiptAddressBean指向的数据
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
                if (checkData()) {
                    if (receiptAddressBean == null) {
                        //数据合法，将数据获取出来放置到bean中，然后将bean插入数据库中。
                        createReceiptAddress();
                    } else {
                        updateReceiptAddress();
                    }
                }
                break;
        }
    }

    private void updateReceiptAddress() {
        //如何将编辑后的信息，更新到数据库中
        //重新获取界面控件中编辑后的内容
        getOrUpdate(true);
        //通过receiptAddressDao更新数据库
        receiptAddressDao.update(receiptAddressBean);
        finish();
    }

    private void getOrUpdate(boolean isUpdate) {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String otherPhone = etPhoneOther.getText().toString().trim();
        String receiptAddress = tvReceiptAddress.getText().toString().trim();
        String detailAddress = etDetailAddress.getText().toString().trim();
        String tvLabelString = tvLabel.getText().toString().trim();
        int checkId = rgSex.getCheckedRadioButtonId();
        String sex = "";
        if (checkId == R.id.rb_man) {
            sex = "男性";
        } else {
            sex = "女性";
        }
        if (!isUpdate) {
            receiptAddressBean = new ReceiptAddressBean();
            receiptAddressBean.set_id(MyApplication.userId);
            receiptAddressBean.setIsSelect(0);
        }
        //将编辑后的内容设置在receiptAddressBean对象中
        receiptAddressBean.setName(name);
        receiptAddressBean.setSex(sex);
        receiptAddressBean.setPhone(phone);
        receiptAddressBean.setPhoneOther(otherPhone);
        receiptAddressBean.setReceiptAddress(receiptAddress);
        receiptAddressBean.setDetailAddress(detailAddress);
        receiptAddressBean.setLabel(tvLabelString);
    }

    private void createReceiptAddress() {
        getOrUpdate(false);
        //结束当前的Activity
        receiptAddressDao.create(receiptAddressBean);
        finish();
    }

    private boolean checkData() {
        String name = etName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(context, "请填写联系人", Toast.LENGTH_SHORT).show();
            return false;
        }
        String phone = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(context, "请填写手机号码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!PhoneUtils.isMobile(phone)) {
            Toast.makeText(context, "请填写合法的手机号", Toast.LENGTH_SHORT).show();
            return false;
        }
        String otherPhone = etPhoneOther.getText().toString().trim();
        String receiptAddress = tvReceiptAddress.getText().toString().trim();
        String detailAddress = etDetailAddress.getText().toString().trim();
        if (TextUtils.isEmpty(detailAddress)) {
            Toast.makeText(context, "请填写详细地址", Toast.LENGTH_SHORT).show();
            return false;
        }
        String tvLabelString = tvLabel.getText().toString().trim();
        if (TextUtils.isEmpty(tvLabelString)) {
            Toast.makeText(context, "请输入标签信息", Toast.LENGTH_SHORT).show();
            return false;
        }
        int checkId = rgSex.getCheckedRadioButtonId();
        if (checkId != R.id.rb_man && checkId != R.id.rb_women) {
            //2个不相等，则说明没有选中任意一个。
            Toast.makeText(context, "请选择性别", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void showLabelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择标签");
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
