package entrance;

import java.io.File;

import lucene.IndexManager;
/**
 * ɾ������main���
 * @author King
 *
 */
public class delIndexEntrance {
	public static void main(String[] args){
		String[] delArray={"����","Hi"};//ɾ���ǲ���,���ǽ���,�ر�ע��
		//���������
	 	IndexManager.deleteDir(new File(IndexManager.INDEX_DIR));
	 	//�ٴ�������
	 	IndexManager.createIndex(IndexManager.DATA_DIR);
	 	//��ɾ���ͺ�����ص�ĳ������
	 	IndexManager.deleteIndexes("content", delArray);
	 	//�������"����",������Ƿ�鵽
	 	IndexManager.searchIndex("����");
	 	//�Ҳ��������,֤��ȷʵɾ����
	 }
}