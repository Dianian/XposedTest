package com.example.xposedtest;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class HookUtil implements IXposedHookLoadPackage{

	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		// ���Ŀ��app����
        if (!lpparam.packageName.equals("com.example.logintest"))
            return;
        XposedBridge.log("Loaded app: " + lpparam.packageName);
        
        // Hook MainActivity�е�isCorrectInfo(String,String)����
        // findAndHookMethod(hook������������classLoader��hook��������hook��������...��XC_MethodHook)
//        XposedHelpers.findAndHookMethod("com.example.logintest.MainActivity", lpparam.classLoader, "isCorrectInfo", String.class,
//                String.class, new XC_MethodHook() {
//
//                    @Override
//                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                        XposedBridge.log("��ʼhook");
//                        XposedBridge.log("����1 = " + param.args[0]);
//                        XposedBridge.log("����2 = " + param.args[1]);
//                    }
//
//                    @Override
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        XposedBridge.log("����hook");
//                        XposedBridge.log("����1 = " + param.args[0]);
//                        XposedBridge.log("����2 = " + param.args[1]);
//
//                    }
//                });
        
        // Hook MainActivity�е�onClick(View v)����
        XposedHelpers.findAndHookMethod("com.example.logintest.MainActivity", lpparam.classLoader, "onClick", View.class, new XC_MethodHook() {

                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                        Class clazz = param.thisObject.getClass();
                        XposedBridge.log("class name:"+clazz.getName());
                        
                        
                        // �����Ϊ˽��private ��ͨ�����·�ʽ��ȡ 
//                        Field field = clazz.getField("et_password");// ��������� id

                        // ͨ������ֽ���õ��������������������ԣ�����˽�л���
                        Field field = clazz.getDeclaredField("et_password");
                        // ���÷���Ȩ�ޣ��������й�android��������Ŀ���˵����Ϥ��
                        field.setAccessible(true);

                        EditText password = (EditText) field.get(param.thisObject);
                        
                        String string = password.getText().toString();
                        XposedBridge.log("���� = " + string);
                        // ����������
                        password.setText("�����ð�!!");
                        
                    }
                });
        
        
        
        
	}

}
