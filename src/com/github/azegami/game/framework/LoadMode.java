package com.github.azegami.game.framework;

//読み込みモード
public enum LoadMode{
	RESOURCE,	//res/ フォルダの下から(音声のみ)
	FILE,				//ファイルシステムから
	ASSET,			//リソース(Assetの中）から
	NOLOAD,		//読み込みはしていない
	MEMORY,		//メモリーから再読み込みはできない
}