package com.study.multithread.reflect;

/**
 * 说明:
 *
 * @USER: Cgh
 * @DATE: 2021/10/15
 */
public class ProgramMonkey extends Person implements Company {

    String mLanguage = "C";
    String mCompany = "VVS";

    public ProgramMonkey(String mName, String mSex, int mAge) {
        super(mName, mSex, mAge);
    }

    public ProgramMonkey(String mName, String mSex, int mAge, String mLanguage, String mCompany) {
        super(mName, mSex, mAge);
        this.mLanguage = mLanguage;
        this.mCompany = mCompany;
    }

    @Override
    public String getCompany() {
        return null;
    }

    public String getmLanguage() {
        return mLanguage;
    }

    public void setmLanguage(String mLanguage) {
        this.mLanguage = mLanguage;
    }

    public String getmCompany() {
        return mCompany;
    }

    public void setmCompany(String mCompany) {
        this.mCompany = mCompany;
    }

    private int getSalaryPerMonth(){
        return 12306;
    }
}
