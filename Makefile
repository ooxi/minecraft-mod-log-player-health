.PHONY:	all
all:	clean	\
	build


.PHONY:	clean
clean:
	if [ -d '.gradle' ]; then	\
		rm -rf '.gradle';	\
	fi

	if [ -d 'build' ]; then		\
		rm -rf 'build';		\
	fi


.PHONY:	build
build:
	./gradlew clean build

