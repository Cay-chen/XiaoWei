package cay.com.xiaowei.VersionUpdate;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 版本更新实体
 * 
 * @author choy
 * 
 */
public class VersionUpdate implements Parcelable {
	/**
	 * 版本更新地址
	 */
	private String URLaddress;

	/**
	 * 版本号，eg:1.22
	 */
	private float Version;
	/**
	 * 是否强制更新 1-强制更新, 0-非强制更新
	 */
	private int ForcedUpdate;

	public static final Creator<VersionUpdate> CREATOR = new Creator<VersionUpdate>() {

		@Override
		public VersionUpdate[] newArray(int size) {

			return new VersionUpdate[size];
		}

		@Override
		public VersionUpdate createFromParcel(Parcel source) {
			VersionUpdate entity = new VersionUpdate();
			entity.URLaddress = source.readString();
			entity.Version = source.readFloat();
			entity.ForcedUpdate = source.readInt();
			return entity;
		}
	};

	@Override
	public int describeContents() {

		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(URLaddress);
		dest.writeFloat(Version);
		dest.writeInt(ForcedUpdate);
	}

	public String getURLaddress() {
		return URLaddress;
	}

	public void setURLaddress(String uRLaddress) {
		URLaddress = uRLaddress;
	}

	public float getVersion() {
		return Version;
	}

	public void setVersion(float version) {
		Version = version;
	}

	public int getForcedUpdate() {
		return ForcedUpdate;
	}

	public void setForcedUpdate(int forcedUpdate) {
		ForcedUpdate = forcedUpdate;
	}
}
