package rxhttp.wrapper.parse;


import java.io.IOException;

import io.reactivex.annotations.NonNull;
import okhttp3.Response;
import okhttp3.ResponseBody;
import rxhttp.wrapper.utils.IOUtil;

/**
 * 文件下载解析器
 * User: ljx
 * Date: 2018/10/23
 * Time: 13:49
 */
public class DownloadParser implements Parser<String> {
    private String mDestPath;

    /**
     * @param destPath 目标路径
     */
    public DownloadParser(@NonNull String destPath) {
        mDestPath = destPath;
    }

    /**
     * @param response Http请求执行结果
     * @return 下载成功后的文件路径
     * @throws IOException 网络异常等,RxJava的观察者会捕获此异常
     */
    @Override
    public String onParse(Response response) throws IOException {
        if (!response.isSuccessful())
            throw new IOException(response.code() + " " + response.message());
        ResponseBody body = response.body();
        if (body == null)
            throw new IOException("ResponseBody is null");
        boolean append = response.header("Content-Range") != null;
        IOUtil.write(body.byteStream(), mDestPath, append);//将输入流写出到文件
        return mDestPath;
    }
}
