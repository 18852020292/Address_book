package com.example.addressbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 *
 * @author 刘杰
 */
public class AddPhoneNumber extends AppCompatActivity {

    /**
     * 定义所需要使用到的button和EditText控件对象
     */
    Button buttonBack;
    Button buttonSave;
    EditText editTextName,
            editTextPhone1,
            editTextPhone2,
            editTextHousePhone,
            editTextOfficePhone,
            editTextAddress,
            editTextRemark;
    /**
     * 定义数据库
     */
    private Datebase datebase;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_phone_number);
        //创建数据库对象
        datebase = new Datebase(this,"PhoneNumber",null,1);
        //创建控件对象
        buttonBack = (Button) findViewById(R.id.back);
        buttonSave = (Button) findViewById(R.id.save);
        editTextName = (EditText) findViewById(R.id.edit_name);
        editTextPhone1 = (EditText)findViewById(R.id.edit_phone_number1);
        editTextPhone2 = (EditText)findViewById(R.id.edit_phone_number2);
        editTextHousePhone = (EditText)findViewById(R.id.edit_house_number);
        editTextOfficePhone = (EditText)findViewById(R.id.edit_office_number);
        editTextAddress = (EditText)findViewById(R.id.edit_address);
        editTextRemark = (EditText)findViewById(R.id.edit_remark);
        //设置点击返回按钮时触发的事件
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //设置事件为返回到主活动中
                Intent intent = new Intent(AddPhoneNumber.this,MainActivity.class);
                startActivity(intent);
            }
        });
        //定义保存按钮被触发是的事件
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                定义字符串，并写获取EditText输入框中的文字
                String editName = editTextName.getText().toString(),
                        editPhone1 = editTextPhone1.getText().toString(),
                        editPhone2 = editTextPhone2.getText().toString(),
                        editHousePhone = editTextHousePhone.getText().toString(),
                        editOfficePhone = editTextOfficePhone.getText().toString(),
                        editAddress = editTextAddress.getText().toString(),
                        editRemark = editTextRemark.getText().toString();
//              判断输入的联系人姓名是否为空和联系方式1、联系方式2、家庭座机、办公座机其中一个是否不为空
                boolean bool1 = (!"".equals(editName)) && (!"".equals(editPhone1) || !"".equals(editPhone2) || !"".equals(editHousePhone) || !"".equals(editOfficePhone));
                if (bool1){
                    //定义一个计数器，用来判断数据库中是否存在此联系人
                    int count = 0;
                    //定义可操作的数据库对象
                    db = datebase.getWritableDatabase();
                    //设置Curso对象，用来查看数据库中的信息
                    Cursor cursor = db.query("PhoneNumber",null,null,null,null,null,null);
                    //判断数据库是否为空
                    if (cursor.moveToFirst()){
                        do{
//  获取数据库中的信息，并且赋值给所定义的字符串，括号内为数据库字段名称
                            String name=cursor.getString(cursor.getColumnIndex("name"));
                            String phone1 = cursor.getString(cursor.getColumnIndex("phone1"));
                            String phone2 = cursor.getString(cursor.getColumnIndex("phone2"));
                            String housePhone = cursor.getString(cursor.getColumnIndex("housePhone"));
                            String officePone = cursor.getString(cursor.getColumnIndex("officePhone"));
                            String address = cursor.getString(cursor.getColumnIndex("address"));
                            String remark = cursor.getString(cursor.getColumnIndex("remark"));

                            //判断数据库中是否已经存在输入的联系人的姓名，或者是否存在输入的信息相同的信息
                            if((name.equals(editName)&& phone1.equals(editPhone1)) && (phone2.equals(editPhone2)
                                    && housePhone.equals(editHousePhone)) && (officePone.equals(editOfficePhone) && address.equals(editAddress) && remark.equals(editRemark)) || name.equals(editName)){
//  如果存在相同的，那么count自增 count ++;
                            }
                        }while (cursor.moveToNext());
                    }


//                    如果输入的信息不相同，也就是count没有进行运算
                    if (count == 0){
//                       定义可写的数据库
                        SQLiteDatabase db = datebase.getWritableDatabase();
//                        创建ContentValues对象
                        ContentValues values = new ContentValues();
//                        调用put方法添加数据到ContentValues对象中
                        values.put("name",editName);
                        values.put("phone1", editPhone1);
                        values.put("phone2", editPhone2);
                        values.put("housePhone", editHousePhone);
                        values.put("officePhone", editOfficePhone);
                        values.put("address",editAddress);
                        values.put("remark", editRemark);
//                        添加数据到数据库表中
                        db.insert("PhoneNumber",null,values);
//                        清楚values的数据
                        values.clear();
//                        提示保存成功
                        Toast.makeText(AddPhoneNumber.this,"保存成功！",Toast.LENGTH_SHORT).show();
//                        跳转回主界面
                        Intent intent = new Intent(AddPhoneNumber.this,MainActivity.class);
                        startActivity(intent);
                    }else{
//                        如果联系人已经存在，提示已经存在
                        Toast.makeText(AddPhoneNumber.this,"联系人已存在！",Toast.LENGTH_SHORT).show();
                    }
                }else{
//                    如果输入的必要信息没有填写，则会提示
                    Toast.makeText(AddPhoneNumber.this,"请填写联系人相关信息！",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}

