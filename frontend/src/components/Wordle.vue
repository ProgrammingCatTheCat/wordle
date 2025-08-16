<script setup>
    import blankImage from "../assets/images/blank.png"
    const image_matrix = Array(6).fill().map(() => Array(5).fill(blankImage))

    const loadLetterImages = async () => {
        const letterImages = {}
        const letters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'.split('')
        
        for (const letter of letters) {
            try {
            const module = await import(`../assets/images/alphabet/${letter}.png`)
            letterImages[letter] = module.default
            } catch (error) {
            console.warn(`Failed to load image for letter ${letter}`)
            }
        }
        
        return letterImages
    }

    const keyboardRows = [
        ['A', 'B', 'C', 'D', 'E', 'F', 'G'],
        ['H', 'I', 'J', 'K', 'L', 'M', 'N'],
        ['O', 'P', 'Q', 'R', 'S', 'T', 'U'],
        ['V', 'W', 'X', 'Y', 'Z']
    ]

    import { ref, onMounted } from 'vue'
    const letterImages = ref({})

    onMounted(async () => {
    letterImages.value = await loadLetterImages()
    })

    const onKeyPress = (letter) => {
    console.log(`Pressed letter: ${letter}`)
    }


</script>

<template>
    <div class = "wordle-container">
        <div 
            v-for="(row, rowIndex) in image_matrix" 
            :key="rowIndex" 
            :id="`wordrow_${rowIndex + 1}`"
            class="input_matrix"
        >
            <img 
                v-for="(src, colIndex) in row" 
                :key="colIndex" 
                :src="src"  
                :id="`word_${rowIndex + 1}_${colIndex + 1}`"  
                :alt="`Blank Image Row ${rowIndex + 1} Col ${colIndex + 1}`"
                class="empty-word-image"
            />
        </div>

        <div class="keyboard-container">
      <div 
        v-for="(keyboardRow, rowIndex) in keyboardRows" 
        :key="rowIndex" 
        class="keyboard-row"
      >
        <img 
          v-for="letter in keyboardRow" 
          :key="letter" 
          :src="letterImages[letter]" 
          :alt="`Letter ${letter}`"
          :id="`key-${letter}`"
          class="keyboard-key"
          @click="onKeyPress(letter)"
        />
      </div>
        </div>
    </div>
</template>

<style scoped>
.wordle-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  width: 100vw; /* 使用视口宽度 */
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.input_matrix {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 500px; /* 明确设置宽度为10个50px图片 + 9个5px间距 = 545px，这里留点余地 */
  margin: 0 auto; /* 自动外边距实现水平居中 */
}

.empty-word-image {
  width: 50px;
  height: 50px;
  padding: 5px;
  margin: 0; /* 移除margin，使用padding控制间距 */
}

.keyboard-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 30px;
  gap: 10px;
}

.keyboard-row {
  display: flex;
  justify-content: center;
  gap: 5px;
}

.keyboard-key {
  width: 50px;
  height: 50px;
  padding: 3px;
  cursor: pointer;
  transition: transform 0.1s ease;
}

.keyboard-key:hover {
  transform: scale(1.1);
}
</style>