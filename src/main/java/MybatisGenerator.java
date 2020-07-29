import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * mybatis逆向工程执行类
 */
public class MybatisGenerator {
    public static void main(String[] args) {
        List<String> warnings = new ArrayList<>();
        String genCfg = "/generatorConfig.xml";
        File configFile = new File(MybatisGenerator.class.getResource(genCfg).getFile());
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = null;
        try {
            config = cp.parseConfiguration(configFile);
        } catch (XMLParserException | IOException e) {
            e.printStackTrace();
        }
        DefaultShellCallback callback = new DefaultShellCallback(true);
        DwMyBatisGenerator myBatisGenerator = null;
        try {
            myBatisGenerator = new DwMyBatisGenerator(Objects.requireNonNull(config), callback, warnings);

        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        try {
            Objects.requireNonNull(myBatisGenerator).generate(null);

        } catch (InterruptedException | IOException | SQLException e) {
            e.printStackTrace();
        }
        DwCommentGenerator.disconnection();
        System.out.println("代码反向生成完毕-----");

    }
}
