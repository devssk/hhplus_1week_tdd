# 동시성 제어 방식에 대한 분석 및 보고서
과제에서 [UserPointTable.java](src%2Fmain%2Fjava%2Fio%2Fhhplus%2Ftdd%2Fdatabase%2FUserPointTable.java)와 [PointHistoryTable.java](src%2Fmain%2Fjava%2Fio%2Fhhplus%2Ftdd%2Fdatabase%2FPointHistoryTable.java)를 사용하면 각각의 클래스에 map과 list로 insert 및 select가 이루어지게 된다.

@Component 어노테이션이 붙어있으므로 스프링 bean에 등록되어 관리되게 되며 이는 싱글톤으로 만들어져 공유자원인 map과 list에 동시에 작업을 할수있게된다.

따라서 동시성 문제가 발생하게 되는데 해결한 방법은 ReentrantLock과 UserId별로 이 Lock을 관리하는 ConcurrentHashMap을 사용하여 동시성 문제를 해결하였다.

[PointServiceLock.java](src%2Fmain%2Fjava%2Fio%2Fhhplus%2Ftdd%2Fpoint%2FPointServiceLock.java)를 만들어 [PointService.java](src%2Fmain%2Fjava%2Fio%2Fhhplus%2Ftdd%2Fpoint%2FPointService.java)에 DI시켰고 해당 클래스에서 charge와 use에 Lock을 적용하였다.

charge와 use에서 같은 UserId에 대해 진행이 되면 Lock을 가져오기 위해 대기하는 과정이 진행이 되므로 동시성에 대한 문제가 해결되며 다른 UserId에 대해선 다른 Lock을 가지게 되므로 다른 UserID와는 영향이 없게 된다.

또한 ReentrantLock에 공정성을 추가하는 ReentrantLock(pair:true)를 적용해서 순서를 어느정도 보장해주었다.