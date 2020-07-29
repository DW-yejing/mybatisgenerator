import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.JDBCConnectionConfiguration;
import org.mybatis.generator.config.xml.ConfigurationParser;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * mybatis实体类注释生成器
 */
public class DwCommentGenerator implements CommentGenerator {
    private static Connection connection;
    private static Connection getConnection(){
        String genCfg = "/generatorConfig.xml";
        File configFile = new File(MybatisGenerator.class.getResource(genCfg).getFile());
        List<String> warnings = new ArrayList<>();
        Configuration config;
        Connection conn = null;
        try {
            ConfigurationParser cp = new ConfigurationParser(warnings);
            config = cp.parseConfiguration(configFile);
            if(config.getContexts()==null || config.getContexts().size()<1){
                return null;
            }
            JDBCConnectionConfiguration jdbcConnectionConfiguration = config.getContexts().get(0).getJdbcConnectionConfiguration();
            String driverClass = jdbcConnectionConfiguration.getDriverClass();
            String url = jdbcConnectionConfiguration.getConnectionURL();
            String username = jdbcConnectionConfiguration.getUserId();
            String password = jdbcConnectionConfiguration.getPassword();
            DriverManager.registerDriver((Driver) Class.forName(driverClass).newInstance());
            conn = DriverManager.getConnection(url, username, password);
            connection = conn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void disconnection(){
        if (connection!=null){
            try{
               connection.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

	@Override
	public void addConfigurationProperties(Properties properties) {
		
	}

	@Override
	public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        try {
            if(DwCommentGenerator.connection==null){
                getConnection();
            }
            Statement stmt = DwCommentGenerator.connection.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "select cast(isnull(e.[value],'') as nvarchar(1024)) as remark from  sys.columns a inner join sys.objects c on a.object_id=c.object_id and c.type='u' left join sys.extended_properties e on e.major_id=c.object_id  and e.minor_id=a.column_id and e.class=1 where c.name= '"+introspectedTable.getFullyQualifiedTable()+"'"+"and a.name = '"+introspectedColumn.getActualColumnName()+"'");
            while (rs.next()) {
                introspectedColumn.setRemarks(rs.getString("remark"));
                StringBuilder sb = new StringBuilder();
                field.addJavaDocLine("/**");
                sb.append(" * ");
                sb.append(introspectedColumn.getRemarks());
                field.addJavaDocLine(sb.toString().replace("\n", " "));
                field.addJavaDocLine(" */");
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	@Override
	public void addFieldComment(Field field, IntrospectedTable introspectedTable) {

	}

	@Override
	public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
	}

	@Override
	public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) {
		
	}

	@Override
	public void addClassComment(InnerClass innerClass,
			IntrospectedTable introspectedTable, boolean markAsDoNotDelete) {
		
	}

	@Override
	public void addEnumComment(InnerEnum innerEnum,
			IntrospectedTable introspectedTable) {
		
	}

	@Override
	public void addGetterComment(Method method,
			IntrospectedTable introspectedTable,
			IntrospectedColumn introspectedColumn) {
		
	}

	@Override
	public void addSetterComment(Method method,
			IntrospectedTable introspectedTable,
			IntrospectedColumn introspectedColumn) {
		
	}

	@Override
	public void addGeneralMethodComment(Method method,
			IntrospectedTable introspectedTable) {
		
	}

	@Override
	public void addJavaFileComment(CompilationUnit compilationUnit) {
		
	}

	@Override
	public void addComment(XmlElement xmlElement) {
		
	}

	@Override
	public void addRootComment(XmlElement rootElement) {
		
	}

    @Override
    public void addGeneralMethodAnnotation(Method method, IntrospectedTable introspectedTable, Set<FullyQualifiedJavaType> set) {

    }

    @Override
    public void addGeneralMethodAnnotation(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn, Set<FullyQualifiedJavaType> set) {

    }

    @Override
    public void addFieldAnnotation(Field field, IntrospectedTable introspectedTable, Set<FullyQualifiedJavaType> set) {

    }

    @Override
    public void addFieldAnnotation(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn, Set<FullyQualifiedJavaType> set) {

    }

    @Override
    public void addClassAnnotation(InnerClass innerClass, IntrospectedTable introspectedTable, Set<FullyQualifiedJavaType> set) {

    }
}
