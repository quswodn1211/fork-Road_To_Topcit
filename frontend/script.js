// ========================================
// TOPCIT 게임 시스템
// ========================================

console.log('🎮 TOPCIT Quest 시작!');

// URL 파라미터 확인 (스토리모드 vs 자유모드)
const urlParams = new URLSearchParams(window.location.search);
const gameMode = urlParams.get('mode'); // 'story' or null(자유모드)
const currentWorld = parseInt(urlParams.get('world')) || null;
const currentStage = parseInt(urlParams.get('stage')) || null;

console.log('게임 모드:', gameMode === 'story' ? '스토리모드' : '자유모드');
if (gameMode === 'story') {
    console.log(`월드 ${currentWorld}, 스테이지 ${currentStage}`);
}

// 게임 상태 관리
let gameState = {
    mode: gameMode,
    world: currentWorld,
    stage: currentStage,
    playerLevel: parseInt(localStorage.getItem('playerLevel')) || 5,
    playerEXP: 0,
    playerMaxEXP: 100,
    currentProblem: null,
    score: 0,
    defeatedEnemies: 0,
    // 스토리모드 전용
    storyHP: 3,
    storyMaxHP: 3,
    correctAnswers: 0,
    totalProblems: 5
};

// 문제 데이터베이스
const problems = [
    {
        id: 1,
        title: "Solve the equation:",
        content: "x² - 3x + 2 = 0",
        description: "위의 이차방정식을 풀어보세요. 근의 공식을 사용하거나 인수분해를 통해 해를 구할 수 있습니다.",
        answer: ["x=1, x=2", "x=2, x=1", "1, 2", "2, 1", "1,2", "2,1"],
        hint: "이 방정식은 (x-1)(x-2) = 0 으로 인수분해할 수 있습니다.",
        exp: 30,
        wrongFeedback: {
            chapter: "1단원 - 수학 기초",
            topic: "이차방정식 풀이",
            detail: "이차방정식 x² - 3x + 2 = 0은 인수분해를 통해 (x-1)(x-2) = 0으로 나타낼 수 있습니다. 따라서 x = 1 또는 x = 2가 해가 됩니다."
        }
    },
    {
        id: 2,
        title: "프로그래밍 문제:",
        content: "배열의 평균을 구하는 함수의 시간 복잡도는?",
        description: "크기가 n인 배열의 모든 원소를 더하고 n으로 나누어 평균을 구하는 알고리즘의 시간 복잡도를 빅오 표기법으로 나타내세요.",
        answer: ["O(n)", "o(n)", "O(N)", "o(N)"],
        hint: "배열의 모든 원소를 한 번씩 방문해야 합니다.",
        exp: 25,
        wrongFeedback: {
            chapter: "2단원 - 알고리즘",
            topic: "시간 복잡도 분석",
            detail: "배열을 순회하는 알고리즘의 시간 복잡도는 배열의 크기 n에 비례합니다."
        }
    },
    {
        id: 3,
        title: "데이터베이스 문제:",
        content: "SELECT * FROM users WHERE age > 20",
        description: "위 SQL 쿼리의 결과는 무엇을 반환하나요?",
        answer: ["나이가 20보다 큰", "age > 20", "20 초과", "20보다 큰"],
        hint: "WHERE 절은 조건을 만족하는 행만 선택합니다.",
        exp: 20,
        wrongFeedback: {
            chapter: "3단원 - 데이터베이스",
            topic: "SQL SELECT 문",
            detail: "WHERE 절은 특정 조건을 만족하는 행만 선택하는 데 사용됩니다."
        }
    },
    {
        id: 4,
        title: "네트워크 문제:",
        content: "TCP와 UDP의 차이점은?",
        description: "TCP는 연결 지향적이고 신뢰성을 보장하는 프로토콜입니다. UDP는?",
        answer: ["비연결", "빠른", "신뢰성 없음", "connectionless"],
        hint: "UDP는 연결 설정 없이 데이터를 전송합니다.",
        exp: 30,
        wrongFeedback: {
            chapter: "4단원 - 네트워크",
            topic: "전송 계층 프로토콜",
            detail: "UDP는 비연결형 프로토콜로 연결 설정 과정이 없어 빠르지만 신뢰성을 보장하지 않습니다."
        }
    },
    {
        id: 5,
        title: "보안 문제:",
        content: "암호화와 해싱의 차이는?",
        description: "암호화는 복호화가 가능하지만, 해싱은?",
        answer: ["불가능", "일방향", "복호화 불가", "단방향"],
        hint: "해시 함수는 원본 데이터를 복원할 수 없습니다.",
        exp: 35,
        wrongFeedback: {
            chapter: "5단원 - 정보보안",
            topic: "암호화 vs 해싱",
            detail: "해싱은 일방향 함수로 원본 데이터를 복원할 수 없습니다."
        }
    }
];

let currentProblemIndex = 0;

// ========================================
// GIF 효과 함수
// ========================================
function flashGif(color = 'white') {
    const gif = document.getElementById('game-gif');
    if (!gif) return;
    
    gif.style.transition = 'filter 0.3s';
    
    if (color === 'green') {
        gif.style.filter = 'brightness(1.5) saturate(1.3)';
    } else if (color === 'red') {
        gif.style.filter = 'brightness(0.7) hue-rotate(330deg)';
    } else {
        gif.style.filter = 'brightness(1.3)';
    }
    
    setTimeout(() => {
        gif.style.filter = 'brightness(1)';
    }, 300);
}

function shakeGif() {
    const gameWorld = document.querySelector('.game-world');
    if (!gameWorld) return;
    
    gameWorld.style.animation = 'shake 0.5s';
    setTimeout(() => {
        gameWorld.style.animation = 'none';
    }, 500);
}

// ========================================
// 경험치 획득 화면 표시
// ========================================
function showExpGain(expAmount) {
    const overlay = document.getElementById('exp-gain-overlay');
    const expAmountElement = document.getElementById('exp-amount');
    
    if (!overlay || !expAmountElement) return;
    
    // 경험치 금액 설정
    expAmountElement.textContent = '+' + expAmount;
    
    // 화면 표시
    overlay.classList.add('show');
    
    // 2초 후 숨김
    setTimeout(() => {
        overlay.classList.remove('show');
    }, 2000);
}

// ========================================
// 오답 피드백 표시
// ========================================
function showWrongFeedback(feedbackData) {
    const section = document.getElementById('wrong-feedback-section');
    const chapterElement = document.getElementById('feedback-chapter');
    const topicElement = document.getElementById('feedback-topic');
    const detailElement = document.getElementById('feedback-detail');
    
    if (!section) return;
    
    // 백엔드 데이터 설정
    chapterElement.textContent = feedbackData.chapter;
    topicElement.textContent = feedbackData.topic;
    detailElement.textContent = feedbackData.detail;
    
    // 화면 표시
    section.classList.add('show');
}

// ========================================
// 오답 피드백 숨김
// ========================================
function hideWrongFeedback() {
    const section = document.getElementById('wrong-feedback-section');
    if (section) {
        section.classList.remove('show');
    }
}

// ========================================
// 페이지 로드 시 초기화
// ========================================
document.addEventListener('DOMContentLoaded', function() {
    console.log('📄 페이지 로드 완료!');
    console.log('🎮 게임 모드:', gameState.mode);
    console.log('🌍 월드:', gameState.world);
    console.log('⭐ 스테이지:', gameState.stage);
    
    // 스토리모드인 경우 HP 표시
    if (gameState.mode === 'story') {
        console.log('💚 스토리모드 - HP 박스 표시!');
        const hpBox = document.getElementById('hp-box');
        if (hpBox) {
            hpBox.style.display = 'flex';
            console.log('✅ HP 박스 표시 완료');
            updateStoryHP();
        } else {
            console.error('❌ HP 박스 요소를 찾을 수 없음!');
        }
    } else {
        console.log('🎯 자유모드 - HP 박스 숨김');
    }
    
    loadProblem(currentProblemIndex);
    updateUI();
    
    // CSS 애니메이션 추가
    addAnimationStyles();
    
    console.log('🚀 게임 준비 완료!');
});

// ========================================
// 게임 로직 함수들
// ========================================

function loadProblem(index) {
    if (index >= problems.length) {
        showCompletionMessage();
        return;
    }
    
    const problem = problems[index];
    gameState.currentProblem = problem;
    
    document.getElementById('problem-title').textContent = problem.title;
    document.getElementById('problem-content').textContent = problem.content;
    document.getElementById('problem-description').textContent = problem.description;
    document.getElementById('answer-input').value = '';
    
    // 오답 피드백 숨김
    hideWrongFeedback();
}

function updateUI() {
    document.getElementById('player-level').textContent = gameState.playerLevel;
}

// 스토리모드 HP 업데이트
function updateStoryHP() {
    const hpHearts = document.getElementById('hp-hearts');
    if (!hpHearts) return;
    
    hpHearts.innerHTML = '';
    
    for (let i = 0; i < gameState.storyMaxHP; i++) {
        const heart = document.createElement('span');
        heart.className = 'heart';
        heart.textContent = '❤️';
        
        if (i >= gameState.storyHP) {
            heart.classList.add('lost');
        }
        
        hpHearts.appendChild(heart);
    }
}

function submitAnswer() {
    console.log('📝 답안 제출!');
    const userAnswer = document.getElementById('answer-input').value.trim();
    
    if (userAnswer === '') {
        alert('답안을 입력해주세요!');
        return;
    }
    
    const problem = gameState.currentProblem;
    const isCorrect = problem.answer.some(answer => 
        userAnswer.toLowerCase().includes(answer.toLowerCase())
    );
    
    console.log('정답 여부:', isCorrect);
    
    if (isCorrect) {
        handleCorrectAnswer(problem);
    } else {
        handleIncorrectAnswer(problem);
    }
}

function handleCorrectAnswer(problem) {
    console.log('✅ 정답!');
    
    // 오답 피드백 숨김
    hideWrongFeedback();
    
    // 경험치 획득
    gameState.playerEXP += problem.exp;
    gameState.defeatedEnemies++;
    gameState.correctAnswers++;
    
    if (gameState.playerEXP >= gameState.playerMaxEXP) {
        levelUp();
    }
    
    gameState.score += 10;
    updateUI();
    
    // GIF 효과
    flashGif('green');
    
    // 경험치 획득 화면 표시
    showExpGain(problem.exp);
    
    // 스토리모드: 5문제 완료 확인
    if (gameState.mode === 'story') {
        if (currentProblemIndex + 1 >= gameState.totalProblems) {
            // 스테이지 클리어!
            setTimeout(() => {
                const totalExp = gameState.correctAnswers * 30; // 문제당 평균 경험치
                window.location.href = `stage-clear.html?world=${gameState.world}&stage=${gameState.stage}&correct=${gameState.correctAnswers}&exp=${totalExp}`;
            }, 2500);
            return;
        }
    }
    
    // 다음 문제로 이동
    setTimeout(() => {
        currentProblemIndex++;
        
        // 자유모드: 문제가 끝나면 처음으로
        if (currentProblemIndex >= problems.length) {
            if (gameState.mode !== 'story') {
                showCompletionMessage();
            }
        } else {
            loadProblem(currentProblemIndex);
        }
    }, 2500);
}

function handleIncorrectAnswer(problem) {
    console.log('❌ 오답!');
    
    // GIF 효과
    flashGif('red');
    shakeGif();
    
    // 스토리모드: HP 감소
    if (gameState.mode === 'story') {
        gameState.storyHP--;
        updateStoryHP();
        
        // HP가 0이 되면 게임 오버
        if (gameState.storyHP <= 0) {
            setTimeout(() => {
                if (confirm('❌ 게임 오버!\n\nHP가 모두 소진되었습니다.\n스테이지를 처음부터 다시 시작하시겠습니까?')) {
                    // 스테이지 재시작
                    window.location.href = `problem-solving.html?mode=story&world=${gameState.world}&stage=${gameState.stage}`;
                } else {
                    // 스테이지 선택 화면으로
                    window.location.href = `story-stages.html?world=${gameState.world}`;
                }
            }, 500);
            return;
        }
    }
    
    // 오답 피드백 표시 (백엔드에서 받아올 데이터)
    showWrongFeedback(problem.wrongFeedback);
}

function levelUp() {
    gameState.playerLevel++;
    gameState.playerEXP = gameState.playerEXP - gameState.playerMaxEXP;
    gameState.playerMaxEXP = Math.floor(gameState.playerMaxEXP * 1.5);
    
    // 레벨 저장
    localStorage.setItem('playerLevel', gameState.playerLevel);
    
    updateUI();
    flashGif('white');
    
    alert('🎉 레벨업! LV ' + gameState.playerLevel);
}

function getHint() {
    const problem = gameState.currentProblem;
    if (problem && problem.hint) {
        alert('💡 힌트: ' + problem.hint);
    }
}

function skipProblem() {
    if (confirm('이 문제를 건너뛰시겠습니까? 경험치를 얻을 수 없습니다.')) {
        hideWrongFeedback();
        
        setTimeout(() => {
            currentProblemIndex++;
            loadProblem(currentProblemIndex);
        }, 500);
    }
}

function showCompletionMessage() {
    alert(`🎓 모든 문제를 완료했습니다!
    
최종 레벨: ${gameState.playerLevel}
총 점수: ${gameState.score}
처치한 적: ${gameState.defeatedEnemies}마리

TOPCIT 준비가 ${gameState.playerLevel >= 10 ? '완료' : '진행 중'}되었습니다!`);
}

// ========================================
// 애니메이션 스타일 추가
// ========================================
function addAnimationStyles() {
    const style = document.createElement('style');
    style.textContent = `
        @keyframes shake {
            0%, 100% { transform: translateX(0); }
            25% { transform: translateX(-10px); }
            50% { transform: translateX(10px); }
            75% { transform: translateX(-10px); }
        }
    `;
    document.head.appendChild(style);
}

// Enter 키로 답안 제출
document.getElementById('answer-input')?.addEventListener('keypress', function(e) {
    if (e.key === 'Enter' && !e.shiftKey) {
        e.preventDefault();
        submitAnswer();
    }
});

console.log('✅ 스크립트 로드 완료!');
